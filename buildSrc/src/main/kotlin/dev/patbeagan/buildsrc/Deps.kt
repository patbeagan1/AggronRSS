package dev.patbeagan.buildsrc

object Deps {
    object Version {
        const val kotlin = "1.6.10"
        const val protocolRss = "0.3.1"
        const val h2 = "2.1.212"
        const val sqlite = "3.36.0.3"
        const val rome = "1.0"
        const val compose = "1.1.0"
    }

    const val protocolRss = "dev.patbeagan1:protocol-rss:${Version.protocolRss}"
    const val h2 = "com.h2database:h2:${Version.h2}"
    const val sqlite = "org.xerial:sqlite-jdbc:${Version.sqlite}"
    const val rome = "rome:rome:${Version.rome}"
}