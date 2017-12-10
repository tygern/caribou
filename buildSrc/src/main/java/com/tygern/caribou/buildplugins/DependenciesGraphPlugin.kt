package com.tygern.caribou.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import org.gradle.api.artifacts.UnknownConfigurationException
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task

class DependenciesGraphPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {

            task("dependenciesGraphDot") {
                group = "DependenciesGraph"
                description = "Generate DOT file"

                mustRunAfter("clean")

                doLast { writeDotfile(dotfileText()) }
            }

            task<Exec>("dependenciesGraph") {
                group = "DependenciesGraph"
                description = "Generate PNG file"

                dependsOn("dependenciesGraphDot")
                workingDir(graphBuildDir)

                commandLine("dot", "-O", "-Tpng", "graph.dot")
            }
        }
    }

    private val Project.graphBuildDir: String
        get() = "$buildDir/dependenciesGraph"

    private fun Project.writeDotfile(text: String) {
        delete(graphBuildDir)
        mkdir(graphBuildDir)

        file("$graphBuildDir/graph.dot").writeText(text)
    }

    private fun Project.dotfileText() = StringBuilder("digraph dependencies {\n")
        .let { builder ->
            subprojects.forEach { subProject ->
                addDependencies(subProject, builder)
            }

            builder
                .append("}\n")
                .toString()
        }

    private fun addDependencies(subProject: Project, dotFileText: StringBuilder) =
        try {
            val compileConfig = subProject.configurations["compile"]
            compileConfig
                .dependencies
                .filter { it is ProjectDependency }
                .map { it as ProjectDependency }
                .forEach {
                    dotFileText.append("  \"${subProject.name}\" -> \"${it.dependencyProject.name}\"\n")
                }
        } catch (ignored: UnknownConfigurationException) {
        }
}