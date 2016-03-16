package io.fester.microservice.api

import io.fester.microservice.ApplicationModule
import scaldi.Module


trait ApiModule extends Module {
  self: ApplicationModule ⇒

  bind[RestService] to injected[RestService]

  bind[PublicApi] to injected[PublicApi]
}
