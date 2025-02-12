package io.micronaut.gradle.openapi


import org.gradle.testkit.runner.TaskOutcome

class OpenApiClientWithKotlinSpec extends AbstractOpenApiWithKotlinSpec {

    def "can generate an kotlin OpenAPI client implementation with clientId (KAPT)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "org.jetbrains.kotlin.kapt" version "$kotlinVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        nameMapping = [test: "changedTest"]
                        clientId = "my-client"
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                kapt "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client("my-client")') }
                .size() == 1
    }

    def "can generate an kotlin OpenAPI client implementation with clientId and clientPath (KAPT)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "org.jetbrains.kotlin.kapt" version "$kotlinVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        nameMapping = [test: "changedTest"]
                        clientId = "my-client"
                        clientPath = true
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                kapt "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client(id = "my-client", path = "\\${my-client.base-path}")') }
                .size() == 1
    }

    def "can generate an kotlin OpenAPI client implementation without clientId (KAPT)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "org.jetbrains.kotlin.kapt" version "$kotlinVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        nameMapping = [test: "changedTest"]
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                kapt "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client("\\${openapi-micronaut-client.base-path}")') }
                .size() == 1
    }

    def "can generate an kotlin OpenAPI client implementation with clientId (KSP)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "com.google.devtools.ksp" version "$kspVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        ksp = true
                        nameMapping = [test: "changedTest"]
                        clientId = "my-client"
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                ksp "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client("my-client")') }
                .size() == 1
    }

    def "can generate an kotlin OpenAPI client implementation with clientId and clientPath (KSP)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "com.google.devtools.ksp" version "$kspVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        ksp = true
                        nameMapping = [test: "changedTest"]
                        clientId = "my-client"
                        clientPath = true
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                ksp "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client(id = "my-client", path = "\\${my-client.base-path}")') }
                .size() == 1
    }

    def "can generate an kotlin OpenAPI client implementation without clientId (KSP)"() {
        given:
        settingsFile << "rootProject.name = 'openapi-client'"
        buildFile << """
            plugins {
                id "io.micronaut.minimal.application"
                id "io.micronaut.openapi"
                id "org.jetbrains.kotlin.jvm" version "$kotlinVersion"
                id "org.jetbrains.kotlin.plugin.allopen" version "$kotlinVersion"
                id "com.google.devtools.ksp" version "$kspVersion"
            }
            
            micronaut {
                version "$micronautVersion"
                openapi {
                    client(file("petstore.json")) {
                        lang = "kotlin"
                        useReactive = true
                        generatedAnnotation = false
                        fluxForArrays = true
                        ksp = true
                        nameMapping = [test: "changedTest"]
                    }
                }
            }
            
            $repositoriesBlock

            dependencies {

                ksp "io.micronaut.serde:micronaut-serde-processor"

                implementation "io.micronaut.serde:micronaut-serde-jackson"
                implementation "io.micronaut.reactor:micronaut-reactor"
                implementation "io.micronaut:micronaut-inject-kotlin"
            }

        """

        withPetstore()

        when:
        def result = build('test')

        then:
        result.task(":generateClientOpenApiApis").outcome == TaskOutcome.SUCCESS
        result.task(":generateClientOpenApiModels").outcome == TaskOutcome.SUCCESS
        result.task(":compileKotlin").outcome == TaskOutcome.SUCCESS

        and:
        file("build/generated/openapi/generateClientOpenApiModels/src/main/kotlin/io/micronaut/openapi/model/Pet.kt").exists()
        def petApiFile = file("build/generated/openapi/generateClientOpenApiApis/src/main/kotlin/io/micronaut/openapi/api/PetApi.kt")
        petApiFile.exists()
        petApiFile.readLines()
                .findAll { it.contains('@Client("\\${openapi-micronaut-client.base-path}")') }
                .size() == 1
    }
}
