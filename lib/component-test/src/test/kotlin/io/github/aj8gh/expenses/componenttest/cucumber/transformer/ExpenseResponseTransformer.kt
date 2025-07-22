package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.DataTableType
import io.github.aj8gh.expenses.componenttest.constant.AMOUNT
import io.github.aj8gh.expenses.componenttest.constant.CATEGORY
import io.github.aj8gh.expenses.componenttest.constant.DATE
import io.github.aj8gh.expenses.componenttest.constant.DESCRIPTION
import io.github.aj8gh.expenses.componenttest.constant.ID
import io.github.aj8gh.expenses.componenttest.constant.PARTY_ID
import io.github.aj8gh.expenses.componenttest.context.Aliases
import io.github.aj8gh.expenses.presentation.model.expense.Category
import io.github.aj8gh.expenses.presentation.model.expense.ExpenseResponse
import java.time.LocalDate

class ExpenseResponseTransformer(
  private val aliases: Aliases,
) {

  @DataTableType
  fun transform(data: Map<String, String>) = ExpenseResponse(
    id = aliases.getOrCreate(data[ID]!!),
    partyId = aliases.getOrCreate(data[PARTY_ID]!!),
    category = Category.valueOf(data[CATEGORY]!!),
    amount = data[AMOUNT]!!.toBigDecimal(),
    date = LocalDate.parse(data[DATE]!!),
    description = data[DESCRIPTION],
  )
}
