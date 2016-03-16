package io.fester.microservice

import com.typesafe.config.{Config, ConfigFactory}
import io.fester.microservice.actor.ActorModule
import io.fester.microservice.api.ApiModule
import scaldi.Module


class ApplicationModule extends Module with ApiModule with ActorModule {

  bind[Config] identifiedBy 'applicationConfig to applicationConfig

  lazy val applicationConfig = ConfigFactory.load()
}
