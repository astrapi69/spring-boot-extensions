/**
 * The MIT License
 *
 * Copyright (C) 2021 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.spring.boot.application

import io.github.astrapi69.file.search.PathFinder
import io.github.astrapi69.spring.boot.application.ApplicationHooks.addDbIfNotExists
import java.util.Properties
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.SpringApplication
import org.springframework.test.context.junit.jupiter.SpringExtension

/** Test class for {@link ApplicationHooks} */
@ExtendWith(SpringExtension::class)
class ApplicationHooksTest {

  private lateinit var application: SpringApplication

  @BeforeEach
  fun setUp() {
    application = Mockito.mock(SpringApplication::class.java)
  }

  /** Tests addDatabaseIfNotExists with YAML and properties files */
  @Test
  @DisplayName("Test addDatabaseIfNotExists with YAML and properties files")
  fun testAddDatabaseIfNotExists() {
    val parent = PathFinder.getRelativePath(PathFinder.getSrcTestResourcesDir(), "config")
    val yamlFilename = "database-config.yaml"
    ApplicationHooks.addDatabaseIfNotExists(application, parent, yamlFilename)

    val propertiesFile =
        PathFinder.getRelativePath(
            PathFinder.getSrcTestResourcesDir(), "config", "database-config.properties")
    ApplicationHooks.addDatabaseIfNotExists(application, propertiesFile)
    // Assertions for creationState and behavior verification
  }

  /** Test for the addDbIfNotExists method with a Properties object */
  @Test
  @DisplayName("Test addDbIfNotExists with a Properties object")
  fun testAddDbIfNotExistsWithProperties() {
    val properties =
        Properties().apply {
          setProperty("app.dbName", "authusers")
          setProperty("app.dbHost", "localhost")
          setProperty("app.dbPort", "5432")
          setProperty("app.dbUsername", "postgres")
          setProperty("app.dbPassword", "postgres")
        }
    application.addDbIfNotExists(properties)
    // Assertions to verify creationState and expected behavior
  }
}
