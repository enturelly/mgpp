package io.github.liplum.mindustry.task

import io.github.liplum.dsl.copyTo
import io.github.liplum.dsl.listProp
import io.github.liplum.dsl.prop
import io.github.liplum.mindustry.*
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

/**
 * Data directory of Mindustry desktop is customizable with env var `$MINDUSTRY_DATA_DIR`.
 */
open class ResolveClient : DefaultTask() {
    val location = project.prop<IGameLoc>()
        @Input get
    val mods = project.listProp<IMod>()
        @Input get
    val gameFile
        @OutputFile get() = location.get().resolveOutputFile()

    init {
        outputs.upToDateWhen {
            gameFile.exists()
        }
    }
    @TaskAction
    fun resolve() {
        when (val loc = location.get()) {
            is GitHubGameLoc -> resolveGitHubGameLoc(loc)
            is LocalGameLoc -> resolveLocalGameLoc(loc)
        }
    }

    fun resolveGitHubGameLoc(loc: GitHubGameLoc) {
        val output = loc.resolveOutputFile()
        if (output.isFile) return
        logger.lifecycle("Downloading $loc...")
        try {
            loc.createDownloadLoc().openInputStream().use {
                it.copyTo(output)
            }
        } catch (e: Exception) {
            // now output is corrupted, delete it
            output.delete()
            throw e
        }
        logger.lifecycle("Downloaded $loc at $output.")
    }

    fun resolveLocalGameLoc(loc: LocalGameLoc) {
        val source = loc.resolveOutputFile()
        if (!source.isFile) {
            throw GradleException("Local game ($source) doesn't exists.")
        }
    }
}