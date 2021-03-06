package org.psliwa.idea.composerJson.intellij.codeAssist.composer

import org.psliwa.idea.composerJson.ComposerBundle
import org.psliwa.idea.composerJson.intellij.codeAssist.InspectionTest
import org.psliwa.idea.composerJson.settings.{ComposerJsonSettings, PatternItem}

class PackageVersionInspectionTest extends InspectionTest {

  val UnboundedVersionConstraintWarning = ComposerBundle.message("inspection.version.unboundVersion")
  val WildcardAndComparisonWarning = ComposerBundle.message("inspection.version.wildcardAndComparison")

  override def setUp(): Unit = {
    super.setUp()

    ComposerJsonSettings(myFixture.getProject).getUnboundedVersionInspectionSettings.clear()
  }

  def testGivenUnboundVersion_thatShouldBeReported() = {
    checkInspection(
      s"""
        |{
        |  "require": {
        |    <warning descr="$UnboundedVersionConstraintWarning">"vendor/pkg": ">=2.1.0"</warning>
        |  }
        |}
      """.stripMargin)
  }

  def testGivenBoundVersion_thatIsOk() = {
    checkInspection(
      """
        |{
        |  "require": {
        |    "vendor/pkg": "2.1.0"
        |  }
        |}
      """.stripMargin)
  }

  def testGivenSemVerBoundedVersion_thatIsOk() = {
    checkInspection(
      """
        |{
        |  "require": {
        |    "vendor/pkg": "~1.4"
        |  }
        |}
      """.stripMargin)
  }

  def testGivenUnboundVersion_givenPackageIsExcluded_thatIsOk() = {
    val pkg = "vendor/pkg"

    ComposerJsonSettings(myFixture.getProject).getUnboundedVersionInspectionSettings.addExcludedPattern(new PatternItem(pkg))

    checkInspection(
      s"""
        |{
        |  "require": {
        |    "$pkg": ">=2.1.0"
        |  }
        |}
      """.stripMargin)
  }

  def testGivenComparisonWildcardedVersion_thatShouldBeReported() = {
    checkInspection(
      s"""
        |{
        |  "require": {
        |    <warning descr="$WildcardAndComparisonWarning">"vendor/pkg": "<2.1.*"</warning>
        |  }
        |}
      """.stripMargin)
  }

  def testGivenComparisonAndWrappedWildcardComboVersion_thatShouldBeReported() = {
    checkInspection(
      s"""
        |{
        |  "require": {
        |    <warning descr="$WildcardAndComparisonWarning">"vendor/pkg": "<2.1.*@dev"</warning>
        |  }
        |}
      """.stripMargin)
  }

  def testGivenComparisonAnWildcardComboInLogicalConstraint_thatShouldBeReported() = {
    checkInspection(
      s"""
        |{
        |  "require": {
        |    <warning descr="$WildcardAndComparisonWarning">"vendor/pkg": ">=2.1.*, <2.2"</warning>
        |  }
        |}
      """.stripMargin)
  }
}
