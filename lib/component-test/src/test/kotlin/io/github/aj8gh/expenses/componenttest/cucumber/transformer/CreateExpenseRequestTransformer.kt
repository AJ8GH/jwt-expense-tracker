package io.github.aj8gh.expenses.componenttest.cucumber.transformer

import io.cucumber.java.DataTableType
import io.github.aj8gh.expenses.componenttest.constant.AMOUNT
import io.github.aj8gh.expenses.componenttest.constant.CATEGORY
import io.github.aj8gh.expenses.componenttest.constant.DATE
import io.github.aj8gh.expenses.componenttest.constant.DESCRIPTION
import io.github.aj8gh.expenses.presentation.model.expense.Category
import io.github.aj8gh.expenses.presentation.model.expense.CreateExpenseRequest
import java.time.LocalDate

class CreateExpenseRequestTransformer {

  @DataTableType
  fun transform(data: Map<String, String>) = CreateExpenseRequest(
    category = Category.valueOf(data[CATEGORY]!!),
    amount = data[AMOUNT]!!.toBigDecimal(),
    date = LocalDate.parse(data[DATE]!!),
    description = data[DESCRIPTION],
  )
}
