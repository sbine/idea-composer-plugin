package org.psliwa.idea.composer.idea.completionContributor

import com.intellij.codeInsight._
import com.intellij.json.JsonLanguage
import com.intellij.openapi.extensions.{Extensions, ExtensionPointName}
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase
import org.psliwa.idea.composer.idea.CompletionContributor

class SuggestionsTest extends LightPlatformCodeInsightFixtureTestCase {

  def testSuggestionsOnTopLevel() = {
    suggestions(
      """
        | {
        | <caret>
        | }
      """.stripMargin,
      Array("name", "type")
    )
  }

  private def suggestions(contents: String, expectedSuggestions: Array[String], unexpectedSuggestions: Array[String] = Array()) = {
    myFixture.configureByText("composer.json", contents)
    myFixture.completeBasic()

    assertContainsElements(myFixture.getLookupElementStrings, expectedSuggestions:_*)
    assertDoesntContain(myFixture.getLookupElementStrings, unexpectedSuggestions:_*)
  }

  def testSuggestionsOnTopLevelWithQuotes() = {
    suggestions(
      """
        | {
        | "<caret>"
        | }
      """.stripMargin,
      Array("name", "type")
    )
  }

  def testSuggestionsOnTopLevelWithQuotes_givenPartialText() = {
    suggestions(
      """
        | {
        | "ty<caret>"
        | }
      """.stripMargin,
      Array("type"),
      Array("name")
    )
  }

  def testSuggestionsOnTopLevelWithoutQuotes_givenPartialText() = {
    suggestions(
      """
        | {
        | ty<caret>
        | }
      """.stripMargin,
      Array("type"),
      Array("name")
    )
  }

  def testEnumSuggestions() = {
    suggestions(
      """
        | {
        |   "type": <caret>
        | }
      """.stripMargin,
      Array("library", "project"),
      Array("name", "type")
    )
  }

  def testEnumSuggestionsWithLandingColon() = {
    suggestions(
      """
        | {
        |   "type": <caret>,
        | }
      """.stripMargin,
      Array("library", "project"),
      Array("name", "type")
    )
  }

  def testEnumSuggestionsWithQuotes() = {
    suggestions(
      """
        | {
        |   "type": "<caret>"
        | }
      """.stripMargin,
      Array("library", "project"),
      Array("name", "type")
    )
  }

  def testEnumSuggestionsWithQuotesAndLandingColon() = {
    suggestions(
      """
        | {
        |   "type": "<caret>",
        | }
      """.stripMargin,
      Array("library", "project"),
      Array("name", "type")
    )
  }

  def testNestedObjectSuggestions() = {
    suggestions(
      """
        |{
        | "support": {
        |   <caret>
        | }
        |}
      """.stripMargin,
      Array("email", "issues"),
      Array("type")
    )
  }

  def testOrSuggestions_firstAlternative() = {
    suggestions(
      """
        |{
        | "license": <caret>
        |}
      """.stripMargin,
      Array("MIT", "BSD")
    )
  }

  def testOrSuggestions_secondAlternative() = {
    suggestions(
      """
        |{
        | "license": [
        |   <caret>
        | ]
        |}
      """.stripMargin,
      Array("MIT", "BSD")
    )
  }

  def testBooleanSuggestions() = {
    suggestions(
      """
        |{
        | "prefer-stable": <caret>
        |}
      """.stripMargin,
      Array("true", "false")
    )
  }

  def testArraySuggestions() = {
    suggestions(
      """
        |{
        | "authors": [
        |   {
        |     <caret>
        |   }
        | ]
        |}
      """.stripMargin,
      Array("name", "email", "homepage")
    )
  }

  def testRequireSuggestions() = {
    import scala.collection.JavaConverters._
    val completionContr = completion.CompletionContributor.forLanguage(JsonLanguage.INSTANCE).asScala
      .filter(_.isInstanceOf[CompletionContributor])
      .map(_.asInstanceOf[CompletionContributor])
      .head

    val packages = List("ps/image-optimizer", "ps/fluent-traversable")
    completionContr.setPackagesLoader(() => packages)

    suggestions(
      """
        |{
        | "require": {
        |   <caret>
        | }
        |}
      """.stripMargin,
      packages.toArray
    )
  }
}