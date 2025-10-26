package io.github.aj8gh.expenses.business.exception

import java.util.*

class ResourceAuthorizationException(
  override val message: String,
  override val cause: Throwable? = null,
) : RuntimeException(message, cause) {

  companion object {
    @JvmStatic
    fun from(partyId: UUID, resourceId: UUID, type: String): ResourceAuthorizationException {
      return ResourceAuthorizationException(
        "Party $partyId is unauthorized to access resource of type $type, id $resourceId"
      )
    }
  }
}
