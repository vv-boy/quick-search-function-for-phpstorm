package com.vv.test.services

import com.intellij.ide.IdeBundle
import com.intellij.ide.actions.searcheverywhere.*
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereFiltersStatisticsCollector.LangFilterCollector
import com.intellij.ide.util.gotoByName.FilteringGotoByModel
import com.intellij.ide.util.gotoByName.GotoSymbolModel2
import com.intellij.ide.util.gotoByName.LanguageRef
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.util.Processor

/**
 * @author Konstantin Bulenkov
 */
class FunctionSearch(event: AnActionEvent) : SymbolSearchEverywhereContributor(event) {
    private val myFilter: PersistentSearchEverywhereContributorFilter<LanguageRef>? = null
    override fun getGroupName(): String {
        return "func"
    }

    override fun getSortWeight(): Int {
        return 501
    }

    override fun fetchWeightedElements(rawPattern: String, progressIndicator: ProgressIndicator, consumer: Processor<in FoundItemDescriptor<Any>>) {

        var filter: String = ""
        if (!rawPattern.isEmpty()) {
            filter = "function " + rawPattern
        }

        super.fetchWeightedElements(filter, progressIndicator, consumer)
    }

    override fun createModel(project: Project): FilteringGotoByModel<LanguageRef> {
        val model = GotoSymbolModel2(project)
        if (myFilter != null) {
            model.setFilterItems(myFilter.selectedElements)
        }
        return model
    }

    override fun getFilterCommand(): SearchEverywhereCommandInfo? {
        return SearchEverywhereCommandInfo("@", IdeBundle.message("search.everywhere.filter.symbols.description"), this)
    }

    override fun getActions(onChanged: Runnable): List<AnAction> {
        return doGetActions(myFilter, LangFilterCollector(), onChanged)
    }

    class Factory : SearchEverywhereContributorFactory<Any> {
        override fun createContributor(initEvent: AnActionEvent): SearchEverywhereContributor<Any> {
            return FunctionSearch(initEvent)
        }
    }
}