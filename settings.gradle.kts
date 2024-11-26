enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IpSearcher"
include(":app")
include(":core:ripe-api")
include(":core:uikit")
include(":features:searcher-by-org")
include(":core:database")
include(":features:ip-organisation-selector")
include(":features:searcher-by-ip")
include(":core:data")
include(":features:organisation-info")
include(":core:common")
