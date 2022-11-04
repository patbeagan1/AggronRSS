package dev.patbeagan.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.io.File
import java.util.Locale
import javax.inject.Inject

/**
 *  Task to generate a graph of the modules
 *
 *  Requires "graphviz" in order to run this task
 *  use: "brew install graphviz"
 */
class ModuleGraphPluginA @Inject constructor() : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("modularizationGraph") {
            doLast {
                ModuleGraphCommand(
                    ModuleGraphCommand.Config(
                        rootModuleName = ":app",
                        ignoredModules = listOf(),
                        bundledModules = listOf("compose"),
                    ),
                    ModuleGraphCommand.GraphVizCommandDelegate()
                ).execute(project)
            }
        }
    }
}

private class ModuleGraphCommand(val commandConfig: Config, val graphVizDelegate: GraphVizCommandDelegate) {
    fun execute(project: Project) {
        val (projectCollection, dependencies) = project.traverseAndCategorizeSubprojects()
        val projects = project.findAllDependencies()
            .sortedBy { it.path }
            .toHashSet()
            .let { LinkedHashSet(it) }
        val dotFilePath = "${project.buildDir}/project.dot"

        GraphVizFileWriter(dotFilePath, project).run {
            start(project)
            declareDotNodes(projects, commandConfig)
            declareDotEdges(dependencies, projectCollection.commonProjects)
            end()
        }

        graphVizDelegate.runExternalDotCommand(project, dotFilePath)
    }

    private fun Project.findAllDependencies(): List<Project> {
        // todo convert this to a tailrec
        val queue = mutableListOf(this)
        val rootProjects = mutableListOf<Project>()
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            rootProjects.add(project)
            queue.addAll(project.childProjects.values)
        }
        return rootProjects
    }

    private fun Project.traverseAndCategorizeSubprojects(): Pair<ProjectCollection, LinkedHashMap<Pair<Project, Project>, List<String>>> {
        val projectCollection = MutableProjectCollection()
        val projects = LinkedHashSet<Project>()
        val dependencies = LinkedHashMap<Pair<Project, Project>, List<String>>()
        val queue = mutableListOf(this)
        while (queue.isNotEmpty()) {
            val project = queue.removeAt(0)
            queue.addAll(project.childProjects.values)

            if (project.name in commandConfig.ignoredModules) {
                continue
            }

            for (config in project.configurations) {
                if (config.name.toLowerCase(Locale.ROOT).contains("test")) continue

                config.dependencies
                    .withType(ProjectDependency::class.java)
                    .map { it.dependencyProject }
                    .filter { it.name !in commandConfig.ignoredModules }
                    .forEach { dependency ->
                        projects.add(project)
                        projects.add(dependency)
                        val graphKey = Pair(project, dependency)
                        val traits = dependencies.computeIfAbsent(graphKey) { ArrayList() }.toMutableList()

                        if (config.name.toLowerCase(Locale.ROOT).endsWith("implementation")) {
                            traits.add("style=solid")
                        }
                    }
            }

            if (project.plugins.hasPlugin("com.android.library") || project.plugins.hasPlugin("com.android.application")) {
                projectCollection.androidProjects.add(project)
            }
            if (project.plugins.hasPlugin("java-library") || project.plugins.hasPlugin("java")) {
                projectCollection.javaProjects.add(project)
            }
        }
        return projectCollection to dependencies
    }

    data class Config(
        /**
         * skip root module from showing as a feature
         */
        val rootModuleName: String,
        /**
         * skip modules because these are for support, not affecting production directly
         */
        val ignoredModules: List<String>,
        /**
         * modules that should be bundled together and represented as a single node.
         */
        val bundledModules: List<String>,
    )

    interface ProjectCollection {
        val commonProjects: List<Project>
        val androidProjects: List<Project>
        val javaProjects: List<Project>
    }

    data class MutableProjectCollection(
        override val commonProjects: MutableList<Project> = mutableListOf(),
        override val androidProjects: MutableList<Project> = mutableListOf(),
        override val javaProjects: MutableList<Project> = mutableListOf(),
    ) : ProjectCollection

    class GraphVizFileWriter(
        val dotFilePath: String,
        val rootProject: Project,
    ) {
        val file = prepareFile(dotFilePath, rootProject)

        fun start(project: Project) {
            file.appendText(
                """
                        digraph {
                            graph [
                                label="${project.name}",
                                labelloc=t,
                                fontsize=30,
                                splines=true,
                                compound=true
                            ];
                            node [
                                style=filled, 
                                fillcolor="#bbbbbb"
                            ];
                            rankdir=TB;
            """.trimIndent()
            )
        }

        fun declareDotNodes(
            projects: HashSet<Project>,
            commandConfig: Config,
        ) {
            val (
                rootModuleName,
                ignoredModules,
                bundleModules,
            ) = commandConfig

            file.appendText("\n  # Projects\n\n")
            projects.filter { project ->
                ignoredModules.none { name ->
                    project.path.contains(name)
                }
            }.forEach { project ->
                val traits = mutableListOf<String>()

                when {
                    project.path == rootModuleName -> "shape=diamond, fillcolor=purple"
                    project.path.contains("uicomponents") -> "shape=box, fillcolor=blue, fontcolor=white"
                    bundleModules.any { project.path.contains(it) } -> "shape=box, fillcolor=green"
                    else -> "shape=box, fillcolor=yellow"
                }.let { traits.add(it) }
                val traitString = traits.joinToString(", ")
                file.appendText("  \"${project.path}\" [$traitString];\n")
            }
        }

        fun declareDotEdges(
            dependencies: LinkedHashMap<Pair<Project, Project>, List<String>>,
            commonProjects: List<Project>,
        ) {
            file.appendText("\n  # Edges (Dependencies)\n\n")
            val linkOnce = mutableListOf<String>()
            for ((key, traits) in dependencies) {
                // Don"t link platform modules to their common counterparts. It destroys the graph.
                when {
                    !commonProjects.contains(key.first) && commonProjects.contains(key.second) -> continue
                    // if it is a module that ALREADY links to a infra-* module, AND it links to a infra-* module too, ignore
                    linkOnce.contains(key.first.toString()) && key.second.toString().contains("infra") -> continue
                    // don"t show infra-* to infra-* links
                    key.first.toString().contains("infra") && key.second.toString().contains("infra") -> continue
                    // Links TO the infra group get a dotted line
                    key.second.toString().contains("infra") -> {
                        file.appendText("  \"${key.first.path}\" -> \":infra group\" [style=dotted, weight=5]\n")
                        linkOnce.add(key.first.toString())
                    }
                    else -> {
                        file.appendText("\"${key.first.path}\" -> \"${key.second.path}\"")
                        if (traits.isNotEmpty()) {
                            file.appendText(" [${traits.joinToString(", ")}]")
                        }
                        file.appendText("\n")
                    }
                }
            }
        }

        fun end() {
            file.appendText("}\n")
        }

        private fun prepareFile(
            dotFilePath: String,
            rootProject: Project,
        ): File = File(dotFilePath).apply {
            delete()
            rootProject.buildDir.mkdirs()
            createNewFile()
        }
    }

    class GraphVizCommandDelegate() {
        fun runExternalDotCommand(project: Project, dotFilePath: String) {
            createClusterSvgFile(project, dotFilePath)
            createSvgFile(project, dotFilePath)
            createPdfFile(project, dotFilePath)
        }

        private fun createClusterSvgFile(project: Project, dotFilePath: String) {
            project.exec {
                val destSvg = "$dotFilePath.cluster.svg"
                commandLine(
                    "neato",
                    dotFilePath,
                    "-Goverlap=false",
                    "-Tsvg",
                    "-o", destSvg
                )
            }
        }

        private fun createPdfFile(project: Project, dotFilePath: String) {
            project.exec {
                val destPdf = "$dotFilePath.pdf"
                commandLine(
                    "dot",
                    dotFilePath,
                    "-Gsize=\"16.52,11.68\"",
                    "-Gratio=\"fill\"",
                    "-Glandscape=false",
                    "-Tpdf",
                    "-o", destPdf,
                )
                println("Project module dependency graph created at $destPdf")
            }
        }

        private fun createSvgFile(project: Project, dotFilePath: String) {
            project.exec {
                val destSvg = "$dotFilePath.svg"
                commandLine(
                    "dot",
                    dotFilePath,
                    "-Gsize=\"16.52,11.68\"",
                    "-Gratio=\"fill\"",
                    "-Glandscape=false",
                    "-Tsvg",
                    "-o", destSvg,
                )
                println("Project module dependency graph created at $destSvg")
            }
        }
    }
}
