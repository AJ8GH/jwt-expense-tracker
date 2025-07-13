package io.github.aj8gh.expenses.componenttest.config

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.names.DuplicateTestNameMode

object ComponentSpecConfig : AbstractProjectConfig() {
  override val globalAssertSoftly = true
  override val duplicateTestNameMode = DuplicateTestNameMode.Error
}
