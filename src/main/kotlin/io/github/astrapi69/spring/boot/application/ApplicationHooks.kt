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

import io.github.astrapi69.collection.properties.PropertiesExtensions
import io.github.astrapi69.jdbc.CreationState
import io.github.astrapi69.jdbc.postgresql.PostgreSQLConnectionsExtensions
import io.github.astrapi69.yaml.YamlToPropertiesExtensions
import java.io.File
import java.util.Properties
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener

/** Provides hooks for performing additional setup tasks when starting a SpringApplication */
object ApplicationHooks {

  /** The current state of the database creation process */
  lateinit var creationState: CreationState

  /**
   * Adds a new database if it does not already exist by reading configuration from a YAML file
   *
   * @param application the Spring application instance
   * @param parent the parent directory where the YAML file is located
   * @param yamlFilename the name of the YAML file containing the database configuration
   */
  fun addDatabaseIfNotExists(application: SpringApplication, parent: File, yamlFilename: String) {
    application.addDbIfNotExists(parent, yamlFilename)
  }

  /**
   * Adds a new database if it does not already exist by reading configuration from a properties
   * file
   *
   * @param application the Spring application instance
   * @param propertiesFile the properties file containing the database configuration
   */
  fun addDatabaseIfNotExists(application: SpringApplication, propertiesFile: File) {
    application.addDbIfNotExists(propertiesFile)
  }

  /**
   * Extension method for SpringApplication that adds a database if it does not already exist, using
   * configuration from a YAML file
   *
   * @param parent the parent directory containing the YAML configuration file
   * @param yamlFilename the name of the YAML configuration file
   */
  fun SpringApplication.addDbIfNotExists(parent: File, yamlFilename: String) {
    this.addDbIfNotExists(YamlToPropertiesExtensions.toProperties(File(parent, yamlFilename)))
  }

  /**
   * Extension method for SpringApplication that adds a database if it does not already exist, using
   * configuration from a properties file
   *
   * @param propertiesFile the properties file containing the database configuration
   */
  fun SpringApplication.addDbIfNotExists(propertiesFile: File) {
    this.addDbIfNotExists(PropertiesExtensions.loadProperties(propertiesFile))
  }

  /**
   * Extension method for SpringApplication that adds a database if it does not already exist, using
   * configuration from the given Properties object
   *
   * @param properties the properties containing the database configuration
   */
  fun SpringApplication.addDbIfNotExists(properties: Properties) {
    this.addListeners(
        ApplicationListener<ApplicationStartingEvent> { _: ApplicationStartingEvent ->
          creationState = PostgreSQLConnectionsExtensions.newDatabase(properties)
        })
  }
}
