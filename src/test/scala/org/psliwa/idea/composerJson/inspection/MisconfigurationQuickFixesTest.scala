package org.psliwa.idea.composerJson.inspection

import org.psliwa.idea.composerJson.ComposerBundle

class MisconfigurationQuickFixesTest extends InspectionTest {

  val FixPreferStable = ComposerBundle.message("inspection.quickfix.setPropertyValue", "prefer-stable", "true")
  val FixMinimumStability = ComposerBundle.message("inspection.quickfix.setPropertyValue", "minimum-stability", "stable")

  override def setUp(): Unit = {
    super.setUp()

    myFixture.enableInspections(classOf[MisconfigurationInspection])
  }

  def testSetPreferStableQuickFix_preferStableIsMissing_createPreferStableProperty() = {
    checkQuickFix(FixPreferStable, 2)(
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev<caret>"
        |}
      """.stripMargin,
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev",
        |  "prefer-stable": true
        |}
      """.stripMargin
    )
  }

  def testSetPreferStableQuickFix_preferStableIsFalse_setTrueValue() = {
    checkQuickFix(FixPreferStable, 3)(
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev<caret>",
        |  "prefer-stable": false
        |}
      """.stripMargin,
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev",
        |  "prefer-stable": true
        |}
      """.stripMargin
    )
  }

  def testSetPreferStableQuickFix_preferStableHasNoValue_setTrueValue() = {
    checkQuickFix(FixPreferStable, 2)(
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev<caret>",
        |  "prefer-stable":
        |}
      """.stripMargin,
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "dev",
        |  "prefer-stable": true
        |}
      """.stripMargin
    )
  }

  def testSetMinimumStabilityQuickFix_setStableMinimumStability() = {
    checkQuickFix(FixMinimumStability, 2)(
      """
        |{
        |  "type": "project<caret>",
        |  "minimum-stability": "dev"
        |}
      """.stripMargin,
      """
        |{
        |  "type": "project",
        |  "minimum-stability": "stable"
        |}
      """.stripMargin
    )
  }
}
