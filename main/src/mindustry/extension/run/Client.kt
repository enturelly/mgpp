@file:JvmMultifileClass
@file:JvmName("ExtensionKt")
@file:Suppress("RemoveRedundantBackticks")

package io.github.liplum.mindustry

import io.github.liplum.mindustry.*
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.ExtensionAware
import java.io.File

class Client : Common()

class AddClientSpec(
    override val proj: Project,
    override val backend: Client,
) : AddCommonSpec<Client>() {

    override fun official(version: String) {
        github(
            user = R.anuken,
            repo = R.mindustry,
            tag = version,
            file = R.officialRelease.client,
        )
    }

    override fun official(version: Notation) {
        when (version) {
            Notation.latest -> backend.location = LatestOfficialMindustryLoc(file = R.officialRelease.client)
            else -> proj.logger.log(LogLevel.WARN, "Version $version is unsupported")
        }
    }

    override fun be(version: String) {
        github(
            user = R.anuken,
            repo = R.mindustryBuilds,
            tag = version,
            file = "Mindustry-BE-Desktop-$version.jar",
        )
    }

    override fun be(version: Notation) {
        when (version) {
            Notation.latest -> backend.location = LatestBeMindustryLoc(file = "Mindustry-BE-Desktop-$version.jar")
            else -> proj.logger.log(LogLevel.WARN, "Version $version is unsupported")
        }
    }

    fun fooClient(
        tag: String,
        file: String,
    ) {
        github(
            user = R.fooClient.user,
            repo = R.fooClient.repo,
            tag = tag,
            file = file,
        )
    }

    fun fooClient(props: Map<String, String>) {
        fooClient(
            tag = props["tag"] ?: "",
            file = props["file"] ?: "",
        )
    }

    fun cnARC(
        tag: String,
        file: String,
    ) {
        github(
            user = R.cnARC.user,
            repo = R.cnARC.repo,
            tag = tag,
            file = file,
        )
    }

    fun cnARC(
        version: String,
    ) {
        github(
            user = R.cnARC.user,
            repo = R.cnARC.repo,
            tag = version,
            file = "Mindustry-CN-ARC-Desktop-$version.jar",
        )
    }

    fun cnARC(props: Map<String, String>) {
        val version = props["version"]
        if(version != null){
            cnARC(version=version)
        }else {    
            cnARC(
                tag = props["tag"] ?: "",
                file = props["file"] ?: "",
            )
        }
    }
}
