/*
 * Copyright 2017 James F. Bowring and CIRDLES.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cirdles.squid.tasks.expressions.builtinExpressions;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.cirdles.squid.constants.Squid3Constants;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_MEAN_PPM_PARENT_NAME;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_PPM_PARENT_EQN_NAME;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_PPM_PARENT_EQN_NAME_TH;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_PPM_PARENT_EQN_NAME_U;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_TH_U_EQN_NAME;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_TH_U_EQN_NAME_S;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_TOTAL_206_238_NAME;
import static org.cirdles.squid.constants.Squid3Constants.SQUID_TOTAL_208_232_NAME;
import org.cirdles.squid.tasks.expressions.Expression;
import org.cirdles.squid.tasks.expressions.constants.ConstantNode;
import org.cirdles.squid.tasks.expressions.expressionTrees.ExpressionTreeInterface;

/**
 *
 * @author James F. Bowring
 */
public abstract class BuiltInExpressionsFactory {

    /**
     * TODO: these guys are hard coded for now, but need to be calculated from
     * reference materials, physical constants, etc.
     *
     * @return
     */
    public static Map<String, ExpressionTreeInterface> generateConstants() {
        Map<String, ExpressionTreeInterface> constants = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        ExpressionTreeInterface r238_235s = new ConstantNode("r238_235s", Squid3Constants.uRatio);//          137.88);
        constants.put(r238_235s.getName(), r238_235s);

        ExpressionTreeInterface squidTrue = new ConstantNode("TRUE", true);
        constants.put(squidTrue.getName(), squidTrue);

        ExpressionTreeInterface squidFalse = new ConstantNode("FALSE", false);
        constants.put(squidFalse.getName(), squidFalse);

        return constants;
    }

    /**
     * TODO: these guys are hard coded for now, but need to be calculated from
     * reference materials, physical constants, etc.
     *
     * @return
     */
    public static Map<String, ExpressionTreeInterface> generateParameters() {
        Map<String, ExpressionTreeInterface> parameters = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        ExpressionTreeInterface stdUPbRatio = new ConstantNode("StdUPbRatio", 0.0906025999827849); // Ref Mat Model
        parameters.put(stdUPbRatio.getName(), stdUPbRatio);

        ExpressionTreeInterface stdPpmU = new ConstantNode("Std_ppmU", 903); // Ref Mat Model
        parameters.put(stdPpmU.getName(), stdPpmU);

        ExpressionTreeInterface std_76 = new ConstantNode("Std_76", Squid3Constants.std_76);//    0.0587838486664528); // Ref Mat Model
        parameters.put(std_76.getName(), std_76);

        ExpressionTreeInterface stdThPbRatio = new ConstantNode("StdThPbRatio", 0.0280476031222372);// Ref Mat Model
        parameters.put(stdThPbRatio.getName(), stdThPbRatio);

        ExpressionTreeInterface stdRad86fact = new ConstantNode("StdRad86fact", Squid3Constants.stdRad86fact);//   0.309567309630921);
        parameters.put(stdRad86fact.getName(), stdRad86fact);

        ExpressionTreeInterface sComm_74 = new ConstantNode("sComm_74", Squid3Constants.sComm0_74);//          15.5773361);
        parameters.put(sComm_74.getName(), sComm_74);

        ExpressionTreeInterface sComm_64 = new ConstantNode("sComm_64", Squid3Constants.sComm0_64);//          17.821);
        parameters.put(sComm_64.getName(), sComm_64);

        ExpressionTreeInterface sComm_84 = new ConstantNode("sComm_84", Squid3Constants.sComm0_84);//          37.5933995);
        parameters.put(sComm_84.getName(), sComm_84);

        ExpressionTreeInterface lambda238 = new ConstantNode("lambda238", Squid3Constants.lambda238);
        parameters.put(lambda238.getName(), lambda238);

        ExpressionTreeInterface lambda232 = new ConstantNode("lambda232", Squid3Constants.lambda232);
        parameters.put(lambda232.getName(), lambda232);

        return parameters;
    }

    /**
     * Ludwig Q3 - additional equations to calculate 232Th/238U Includes Ludwig
     * Q4 definitions calls
     *
     * @param parentNuclide
     * @param isDirectAltPD
     * @return
     */
    public static SortedSet<Expression> generatePpmUandPpmTh(String parentNuclide, boolean isDirectAltPD) {
        SortedSet<Expression> concentrationExpressionsOrdered = new TreeSet<>();

        if (!isDirectAltPD) {
            // TODO: promote this and tie to physical constants model
            // handles SecondaryParentPpmFromThU
            String uConstant = "((238/232) * r238_235s / (r238_235s - 1.0))";

            String ppmEqnName = SQUID_PPM_PARENT_EQN_NAME_U;
            String ppmEquation = "[\"" + SQUID_PPM_PARENT_EQN_NAME + "\"] / [\"" + SQUID_MEAN_PPM_PARENT_NAME + "\"] * Std_ppmU";

            String ppmOtherEqnName = SQUID_PPM_PARENT_EQN_NAME_TH;
            String ppmOtherEqn = "[\"" + SQUID_PPM_PARENT_EQN_NAME_U + "\"] * [\"" + SQUID_TH_U_EQN_NAME + "\"] / " + uConstant;

            if (parentNuclide.contains("232")) {
                ppmEqnName = SQUID_PPM_PARENT_EQN_NAME_TH;

                ppmOtherEqnName = SQUID_PPM_PARENT_EQN_NAME_U;
                ppmOtherEqn = "[\"" + SQUID_PPM_PARENT_EQN_NAME_TH + "\"] / [\"" + SQUID_TH_U_EQN_NAME + "\"] * " + uConstant;
            }

            Expression expressionPpmU = buildExpression(ppmEqnName,
                    ppmEquation, true, true, false);
            concentrationExpressionsOrdered.add(expressionPpmU);

            Expression expressionPpmTh = buildExpression(ppmOtherEqnName,
                    ppmOtherEqn, true, true, false);
            concentrationExpressionsOrdered.add(expressionPpmTh);

            if (!parentNuclide.contains("232")) {
                concentrationExpressionsOrdered.addAll(generate204207MeansAndAgesForRefMaterialsU());
                concentrationExpressionsOrdered.addAll(generate208MeansAndAgesForRefMaterialsU());
            } else {
                concentrationExpressionsOrdered.addAll(generate204207MeansAndAgesForRefMaterialsTh());
            }

        } else {
            // directlAltPD is true
            concentrationExpressionsOrdered.addAll(generate204207MeansAndAgesForRefMaterialsU());
            concentrationExpressionsOrdered.addAll(generate204207MeansAndAgesForRefMaterialsTh());
            // for unknown samples
            concentrationExpressionsOrdered.addAll(generatePbIsotope204207CorrectionsForU());
            concentrationExpressionsOrdered.addAll(generatePbIsotope204207CorrectionsForTh());

            // math for ThUfromA1A2 follows here
            // for 204Pb ref material
            String exp238 = "( EXP ( Lambda238 * [\"4-corr206Pb/238U Age\"] ) - 1 )";
            String exp232 = "( EXP ( Lambda232 * [\"4-corr208Pb/232Th Age\"] ) - 1 )";
            Expression expression4corrSQUID_TH_U_EQN_NAME = buildExpression("4-corr" + SQUID_TH_U_EQN_NAME,
                    "[\"4-corr208Pb*/206Pb*\"] * " + exp238 + " / " + exp232, true, false, false);
            concentrationExpressionsOrdered.add(expression4corrSQUID_TH_U_EQN_NAME);

            Expression expression4corrSQUID_TH_U_EQN_NAMEpErr = buildExpression("4-corr" + SQUID_TH_U_EQN_NAME + " %err",
                    "SQRT( [\"4-corr208Pb*/206Pb* %err\"]^2 + \n"
                    + "[\"4-corr206Pb/238Ucalibr.const %err\"]^2 +\n"
                    + "[\"4-corr208Pb/232Thcalibr.const %err\"]^2 ) ", true, false, false);
            concentrationExpressionsOrdered.add(expression4corrSQUID_TH_U_EQN_NAMEpErr);

            // for 207Pb ref material
            exp238 = "( EXP ( Lambda238 * [\"7-corr206Pb/238U Age\"] ) - 1 )";
            exp232 = "( EXP ( Lambda232 * [\"7-corr208Pb/232Th Age\"] ) - 1 )";
            Expression expression7corrSQUID_TH_U_EQN_NAME = buildExpression("7-corr" + SQUID_TH_U_EQN_NAME,
                    "[\"7-corr208Pb*/206Pb*\"] * " + exp238 + " / " + exp232, true, false, false);
            concentrationExpressionsOrdered.add(expression7corrSQUID_TH_U_EQN_NAME);

            Expression expression7corrSQUID_TH_U_EQN_NAMEpErr = buildExpression("7-corr" + SQUID_TH_U_EQN_NAME + " %err",
                    "SQRT( [\"7-corr208Pb*/206Pb* %err\"]^2 + \n"
                    + "[\"7-corr206Pb/238Ucalibr.const %err\"]^2 + \n"
                    + "[\"7-corr208Pb/232Thcalibr.const %err\"]^2 ) ", true, false, false);
            concentrationExpressionsOrdered.add(expression7corrSQUID_TH_U_EQN_NAMEpErr);

            // for samples
            Expression expression4corrSQUID_TH_U_EQN_NAMEs = buildExpression("4-corr" + SQUID_TH_U_EQN_NAME_S,
                    "[\"208/206\"] * [\"4-corrTotal 206Pb/238U\"] / [\"4-corrTotal 208Pb/232Th\"]", false, true, false);
            concentrationExpressionsOrdered.add(expression4corrSQUID_TH_U_EQN_NAMEs);

            Expression expression7corrSQUID_TH_U_EQN_NAMEs = buildExpression("7-corr" + SQUID_TH_U_EQN_NAME_S,
                    "[\"208/206\"] * [\"7-corrTotal 206Pb/238U\"] / [\"7-corrTotal 208Pb/232Th\"]", false, true, false);
            concentrationExpressionsOrdered.add(expression7corrSQUID_TH_U_EQN_NAMEs);

            Expression expression4corrSQUID_TH_U_EQN_NAMEsPerr = buildExpression("4-corr" + SQUID_TH_U_EQN_NAME_S + " %err",
                    "SQRT( [%\"208/206\"]^2 + [\"4-corrTotal 206Pb/238U %err\"]^2 + \n"
                    + "[\"4-corrTotal 208Pb/232Th %err\"]^2 )", false, true, false);
            concentrationExpressionsOrdered.add(expression4corrSQUID_TH_U_EQN_NAMEsPerr);

            Expression expression7corrSQUID_TH_U_EQN_NAMEsPerr = buildExpression("7-corr" + SQUID_TH_U_EQN_NAME_S + " %err",
                    "SQRT( [%\"208/206\"]^2 + [\"7-corrTotal 206Pb/238U %err\"]^2 + \n"
                    + "[\"7-corrTotal 208Pb/232Th %err\"]^2 )", false, true, false);
            concentrationExpressionsOrdered.add(expression7corrSQUID_TH_U_EQN_NAMEsPerr);

            // placeholder expressions
            Expression expressionSQUID_TH_U_EQN_NAME = buildExpression(SQUID_TH_U_EQN_NAME,
                    "1", true, false, false);
            concentrationExpressionsOrdered.add(expressionSQUID_TH_U_EQN_NAME);

            Expression expressionSQUID_TH_U_EQN_NAMEs = buildExpression(SQUID_TH_U_EQN_NAME_S,
                    "1", false, true, false);
            concentrationExpressionsOrdered.add(expressionSQUID_TH_U_EQN_NAMEs);

            Expression expressionSQUID_TH_U_EQN_NAMEPerr = buildExpression(SQUID_TH_U_EQN_NAME + " %err",
                    "1", true, false, false);
            concentrationExpressionsOrdered.add(expressionSQUID_TH_U_EQN_NAMEPerr);

            Expression expressionSQUID_TH_U_EQN_NAMEsPerr = buildExpression(SQUID_TH_U_EQN_NAME_S + " %err",
                    "1", false, true, false);
            concentrationExpressionsOrdered.add(expressionSQUID_TH_U_EQN_NAMEsPerr);

        } // end test of directAltD
        return concentrationExpressionsOrdered;
    }

    private static SortedSet<Expression> generatePbIsotope204207CorrectionsForU() {
        SortedSet<Expression> pbIsotopeCorrectionsForU = new TreeSet<>();

        // calculate 204 PbU flavor from Tot68_82_fromA
        Expression expression4corrTotal206Pb238U = buildExpression("4-corrTotal 206Pb/238U",
                "[\"UncorrPb/Uconst\"] / [\"4-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio", false, true, false);
        pbIsotopeCorrectionsForU.add(expression4corrTotal206Pb238U);

        String t2 = "[\"4-corr206Pb/238Ucalibr.const WM\"][2] / [\"4-corr206Pb/238Ucalibr.const WM\"][0] * 100";
        Expression expression4corrTotal206Pb238UpErr = buildExpression("4-corrTotal 206Pb/238U %err",
                "SQRT( [%\"UncorrPb/Uconst\"]^2 + (" + t2 + ")^2 )", false, true, false);
        pbIsotopeCorrectionsForU.add(expression4corrTotal206Pb238UpErr);

        // calculate 207 PbU flavor from Tot68_82_fromA
        Expression expression7corrTotal206Pb238U = buildExpression("7-corrTotal 206Pb/238U",
                "[\"UncorrPb/Uconst\"] / [\"7-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio", false, true, false);
        pbIsotopeCorrectionsForU.add(expression7corrTotal206Pb238U);

        t2 = "[\"7-corr206Pb/238Ucalibr.const WM\"][2] / [\"7-corr206Pb/238Ucalibr.const WM\"][0] * 100";
        Expression expression7corrTotal206Pb238UpErr = buildExpression("7-corrTotal 206Pb/238U %err",
                "SQRT( [%\"UncorrPb/Uconst\"]^2 + (" + t2 + ")^2 )", false, true, false);
        pbIsotopeCorrectionsForU.add(expression7corrTotal206Pb238UpErr);

        // placeholder expressions
        Expression expressionSQUID_TOTAL_206_238_NAME = buildExpression(SQUID_TOTAL_206_238_NAME,
                "1", false, true, false);
        pbIsotopeCorrectionsForU.add(expressionSQUID_TOTAL_206_238_NAME);

        Expression expressionSQUID_TOTAL_206_238_NAMEPerr = buildExpression(SQUID_TOTAL_206_238_NAME + " %err",
                "1", false, true, false);
        pbIsotopeCorrectionsForU.add(expressionSQUID_TOTAL_206_238_NAMEPerr);

        return pbIsotopeCorrectionsForU;
    }

//    private static SortedSet<Expression> generatePbIsotope208CorrectionsForU() {
//        SortedSet<Expression> pbIsotopeCorrectionsForU = new TreeSet<>();
//
//        // calculate 208 PbU flavor from Tot68_82_fromA
//        Expression expression8corrTotal206Pb238U = buildExpression("8-corrTotal 206Pb/238U",
//                "[\"UncorrPb/Uconst\"] / [\"8-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio", false, true, false);
//        pbIsotopeCorrectionsForU.add(expression8corrTotal206Pb238U);
//
//        String t2 = "[\"8-corr206Pb/238Ucalibr.const WM\"][2] / [\"8-corr206Pb/238Ucalibr.const WM\"][0] * 100";
//        Expression expression8corrTotal206Pb238UpErr = buildExpression("8-corrTotal 206Pb/238U %err",
//                "SQRT( [%\"UncorrPb/Uconst\"]^2 + (" + t2 + ")^2 )", false, true, false);
//        pbIsotopeCorrectionsForU.add(expression8corrTotal206Pb238UpErr);
//
//        return pbIsotopeCorrectionsForU;
//    }
    private static SortedSet<Expression> generatePbIsotope204207CorrectionsForTh() {
        SortedSet<Expression> pbIsotopeCorrectionsForTh = new TreeSet<>();

        // calculate 204 PbTh flavors from Tot68_82_fromA
        Expression expression4corrTotal208Pb232Th = buildExpression("4-corrTotal 208Pb/232Th",
                "[\"UncorrPb/Thconst\"] / [\"4-corr208Pb/232Thcalibr.const WM\"][0] * StdThPbRatio", false, true, false);
        pbIsotopeCorrectionsForTh.add(expression4corrTotal208Pb232Th);

        String t2 = "[\"4-corr208Pb/232Thcalibr.const WM\"][2] / [\"4-corr208Pb/232Thcalibr.const WM\"][0] * 100";
        Expression expression4corrTotal208Pb232ThpErr = buildExpression("4-corrTotal 208Pb/232Th %err",
                "SQRT( [%\"UncorrPb/Thconst\"]^2 + (" + t2 + ")^2 )", false, true, false);
        pbIsotopeCorrectionsForTh.add(expression4corrTotal208Pb232ThpErr);

        // calculate 207 PbTh flavors from Tot68_82_fromA
        Expression expression7corrTotal208Pb232Th = buildExpression("7-corrTotal 208Pb/232Th",
                "[\"UncorrPb/Thconst\"] / [\"7-corr208Pb/232Thcalibr.const WM\"][0] * StdThPbRatio", false, true, false);
        pbIsotopeCorrectionsForTh.add(expression7corrTotal208Pb232Th);

        t2 = "[\"7-corr208Pb/232Thcalibr.const WM\"][2] / [\"7-corr208Pb/232Thcalibr.const WM\"][0] * 100";
        Expression expression7corrTotal208Pb232ThpErr = buildExpression("7-corrTotal 208Pb/232Th %err",
                "SQRT( [%\"UncorrPb/Thconst\"]^2 + (" + t2 + ")^2 )", false, true, false);
        pbIsotopeCorrectionsForTh.add(expression7corrTotal208Pb232ThpErr);

        // placeholder expressions
        Expression expressionSQUID_TOTAL_208_232_NAME = buildExpression(SQUID_TOTAL_208_232_NAME,
                "1", false, true, false);
        pbIsotopeCorrectionsForTh.add(expressionSQUID_TOTAL_208_232_NAME);

        Expression expressionSQUID_TOTAL_208_232_NAMEPerr = buildExpression(SQUID_TOTAL_208_232_NAME + " %err",
                "1", false, true, false);
        pbIsotopeCorrectionsForTh.add(expressionSQUID_TOTAL_208_232_NAMEPerr);

        return pbIsotopeCorrectionsForTh;
    }

    /**
     * TODO: These should probably be segregated to the end of the expression
     * list and not be sorted each time?
     *
     * Ludwig Q4 part 1
     *
     * @return
     */
    public static SortedSet<Expression> generateOverCountExpressions() {
        SortedSet<Expression> overCountExpressionsOrdered = new TreeSet<>();

        Expression expressionOverCount4_6_7 = buildExpression("204/206 (fr. 207)",
                "([\"207/206\"] - Std_76 ) / ( sComm_74  - (Std_76 * sComm_64)) ", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCount4_6_7);

        Expression expressionOverCount4_6_7U = buildExpression("204/206 (fr. 207) %err",
                "ABS( [%\"207/206\"] * [\"207/206\"] / ([\"207/206\"] - Std_76) )", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCount4_6_7U);

        Expression expressionOverCount4_6_8 = buildExpression("204/206 (fr. 208)",
                "( [\"208/206\"] - StdRad86fact * [\"232Th/238U\"] ) / (sComm_84 - StdRad86fact * [\"232Th/238U\"] * sComm_64 )", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCount4_6_8);

        Expression expressionOverCountPerSec4_7 = buildExpression("204 overcts/sec (fr. 207)",
                "TotalCps([\"204\"]) - TotalCps([\"BKG\"]) - [\"204/206 (fr. 207)\"] * ( TotalCps([\"206\"]) - TotalCps([\"BKG\"]))", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCountPerSec4_7);

        Expression expressionOverCountPerSec4_8 = buildExpression("204 overcts/sec (fr. 208)",
                "TotalCps([\"204\"]) - TotalCps([\"BKG\"]) - [\"204/206 (fr. 208)\"] * ( TotalCps([\"206\"]) - TotalCps([\"BKG\"]))", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCountPerSec4_8);

        Expression expressionOverCount7CorrCalib = buildExpression("7-corrPrimary calib const. delta%",
                "100 * ( (1 - sComm_64 * [\"204/206\"]) / (1 - sComm_64 * [\"204/206 (fr. 207)\"]) - 1 )", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCount7CorrCalib);

        Expression expressionOverCount8CorrCalib = buildExpression("8-corrPrimary calib const. delta%",
                "100 * ( (1 - sComm_64 * [\"204/206\"]) / (1 - sComm_64 * [\"204/206 (fr. 208)\"]) - 1 ) ", true, false, false);
        overCountExpressionsOrdered.add(expressionOverCount8CorrCalib);

        return overCountExpressionsOrdered;
    }

    /**
     * TODO: These should probably be segregated to the end of the expression
     * list and not be sorted each time?
     *
     * Ludwig Q4 - part 2
     *
     * @return the
     * java.util.SortedSet<org.cirdles.squid.tasks.expressions.Expression>
     */
    public static SortedSet<Expression> generateExperimentalExpressions() {
        SortedSet<Expression> experimentalExpressions = new TreeSet<>();

        Expression dummy = buildExpression("DUMMY", "5", true, true, false);
        dummy.getExpressionTree().setSquidSpecialUPbThExpression(false);
        experimentalExpressions.add(dummy);

        Expression dummyS = buildExpression("DUMMYsum", "5", true, true, true);
        dummyS.getExpressionTree().setSquidSpecialUPbThExpression(false);
        experimentalExpressions.add(dummyS);

        return experimentalExpressions;
    }

    public static SortedSet<Expression> generateSampleDates() {
        // this method is temporary to create these objects until I get the
        // related math
        SortedSet<Expression> sampleDatesOrdered = new TreeSet<>();

        Expression expression204corr206Pb238UAge = buildExpression("204corr206Pb/238UAge",
                "0", false, true, false);
        sampleDatesOrdered.add(expression204corr206Pb238UAge);

        Expression expression207corr206Pb238UAge = buildExpression("207corr206Pb/238UAge",
                "0", false, true, false);
        sampleDatesOrdered.add(expression207corr206Pb238UAge);

        Expression expression208corr206Pb238UAge = buildExpression("208corr206Pb/238UAge",
                "0", false, true, false);
        sampleDatesOrdered.add(expression208corr206Pb238UAge);

        return sampleDatesOrdered;
    }

    /**
     * Squid2.5 Framework: Part 4 up to means
     *
     * @return
     */
    public static SortedSet<Expression> generatePerSpotProportionsOfCommonPb() {
        SortedSet<Expression> perSpotPbCorrectionsOrdered = new TreeSet<>();

        // Calculate a couple of "SampleData-only" columns:
        Expression expression7Corr46 = buildExpression("7-corr204Pb/206Pb",
                "Pb46cor7( [\"207/206\"], [\"207corr206Pb/238UAge\"] )", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression7Corr46);

        Expression expression8Corr46 = buildExpression("8-corr204Pb/206Pb",
                "Pb46cor8( [\"208/206\"], [\"232Th/238U\"], [\"208corr206Pb/238UAge\"] )", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression8Corr46);

        /**
         * calculate ALL of the applicable proportions of common Pb (which in
         * turn reflect all the permutations of index isotope (204-, 207-, and
         * 208-corrected) and daughter-Pb isotope (206Pb and 208Pb, for Pb/U and
         * Pb/Th respectively), firstly for the StandardData sheet and secondly
         * for the SampleData sheet:
         *
         */
        // for ref materials
        Expression expression4corCom206 = buildExpression("4-corr%com206",
                "100 * sComm_64 * [\"204/206\"]", true, true, false);
        perSpotPbCorrectionsOrdered.add(expression4corCom206);

        Expression expression7corCom206 = buildExpression("7-corr%com206",
                "100 * sComm_64 * [\"204/206 (fr. 207)\"]", true, false, false);
        perSpotPbCorrectionsOrdered.add(expression7corCom206);

        Expression expression8corCom206 = buildExpression("8-corr%com206",
                "100 * sComm_64 * [\"204/206 (fr. 208)\"]", true, false, false);
        perSpotPbCorrectionsOrdered.add(expression8corCom206);

        Expression expression4corCom208 = buildExpression("4-corr%com208",
                "100 * sComm_84 / [\"208/206\"] * [\"204/206\"]", true, true, false);
        perSpotPbCorrectionsOrdered.add(expression4corCom208);

        Expression expression7corCom208 = buildExpression("7-corr%com208",
                "100 * sComm_84 / [\"208/206\"] * [\"204/206 (fr. 207)\"]", true, false, false);
        perSpotPbCorrectionsOrdered.add(expression7corCom208);

        // for samples
        Expression expression7corCom206S = buildExpression("7-corr%com206S",
                "100 * sComm_64 * [\"7-corr204Pb/206Pb\"]", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression7corCom206S);

        Expression expression8corCom206S = buildExpression("8-corr%com206S",
                "100 * sComm_64 * [\"8-corr204Pb/206Pb\"]", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression8corCom206S);

        // The next step is to calculate all the applicable radiogenic 208Pb/206Pb values. 
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr208Pb*/206Pb*  *** Start
        Expression expression4corr208Pb206Pb = buildExpression("4-corr208Pb*/206Pb*",
                "( [\"208/206\"] / [\"204/206\"] - sComm_84 ) / ( 1 / [\"204/206\"] - sComm_64)", true, true, false);
        perSpotPbCorrectionsOrdered.add(expression4corr208Pb206Pb);

        // ref material and sample version
        Expression expression4corr208Pb206Pberr = buildExpression("4-corr208Pb*/206Pb* %err",
                "100 * sqrt( ( ([%\"208/206\"] / 100 * [\"208/206\"])^2 +\n"
                + "([\"4-corr208Pb*/206Pb*\"] * sComm_64 - sComm_84)^2 * ([%\"204/206\"] / 100 * [\"204/206\"])^2 )  \n"
                + " / (1 - sComm_64 * [\"204/206\"]) ^2  ) / abs( [\"4-corr208Pb*/206Pb*\"] ) ", true, true, false);
        perSpotPbCorrectionsOrdered.add(expression4corr208Pb206Pberr);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr208Pb*/206Pb*  *** End
        //
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 7-corr208Pb*/206Pb*  *** Start
        // ref material version
        Expression expression7corr208Pb206Pb = buildExpression("7-corr208Pb*/206Pb*",
                "( [\"208/206\"] / [\"204/206 (fr. 207)\"] - sComm_84 ) / \n"
                + "( 1 / [\"204/206 (fr. 207)\"] - sComm_64) ", true, false, false);
        perSpotPbCorrectionsOrdered.add(expression7corr208Pb206Pb);

        // sample version
        Expression expression7corr208Pb206PbS = buildExpression("7-corr208Pb*/206Pb*S",
                "( [\"208/206\"] / [\"7-corr204Pb/206Pb\"] - sComm_84 ) / \n"
                + "( 1 / [\"7-corr204Pb/206Pb\"] - sComm_64)  ", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression7corr208Pb206PbS);

        // ref material version
        Expression expression7corr208Pb206PbPctErr = buildExpression("7-corr208Pb*/206Pb* %err",
                "StdPb86radCor7per([\"208/206\"], [\"207/206\"], [\"7-corr208Pb*/206Pb*\"], [\"204/206 (fr. 207)\"])", true, false, false);
        perSpotPbCorrectionsOrdered.add(expression7corr208Pb206PbPctErr);

        // sample material version
        Expression expression7corr208Pb206PbSPctErr = buildExpression("7-corr208Pb*/206Pb*S %err",
                "Pb86radCor7per([\"208/206\"], [\"207/206\"], [\"7-corrTotal 206Pb/238U\"], \n"
                + "[\"7-corrTotal 206Pb/238U %err\"],[\"207corr206Pb/238UAge\"])", false, true, false);
        perSpotPbCorrectionsOrdered.add(expression7corr208Pb206PbSPctErr);

        return perSpotPbCorrectionsOrdered;
    }

    /**
     * Squid2.5 Framework: Part 4 means
     *
     * @return
     */
    public static SortedSet<Expression> generate204207MeansAndAgesForRefMaterialsU() {
        SortedSet<Expression> meansAndAgesForRefMaterials = new TreeSet<>();

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr  206/238  *** Start
        Expression expression4corr206Pb238Ucalibrconst = buildExpression("4-corr206Pb/238Ucalibr.const",
                "(1 - [\"204/206\"] * sComm_64) * [\"UncorrPb/Uconst\"]", true, true, false);
        meansAndAgesForRefMaterials.add(expression4corr206Pb238Ucalibrconst);

        Expression expression4corr206Pb238Ucalibrconsterr = buildExpression("4-corr206Pb/238Ucalibr.const %err",
                "sqrt([%\"UncorrPb/Uconst\"]^2 + \n"
                + "( sComm_64 / ( 1 / [\"204/206\"] - sComm_64 ) )^2 * [%\"204/206\"]^2 )", true, true, false);
        meansAndAgesForRefMaterials.add(expression4corr206Pb238Ucalibrconsterr);

        // weighted mean
        Expression expression4corr206Pb238UcalibrconstWM = buildExpression("4-corr206Pb/238Ucalibr.const WM",
                "WtdMeanACalc( [\"4-corr206Pb/238Ucalibr.const\"], [\"4-corr206Pb/238Ucalibr.const %err\"], FALSE, FALSE )", true, false, true);
        meansAndAgesForRefMaterials.add(expression4corr206Pb238UcalibrconstWM);

        // age calc
        Expression expression4corr206Pb238UAge = buildExpression("4-corr206Pb/238U Age",
                "LN( 1.0 + [\"4-corr206Pb/238Ucalibr.const\"] / [\"4-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio ) / lambda238", true, false, false);
        meansAndAgesForRefMaterials.add(expression4corr206Pb238UAge);

        Expression expression4corr206Pb238UAgeUnct = buildExpression("4-corr206Pb/238U Age 1sigma",
                "[\"4-corr206Pb/238Ucalibr.const %err\"] / 100 * ( EXP(lambda238 * [\"4-corr206Pb/238U Age\"] ) - 1 ) / lambda238 / EXP(lambda238 * [\"4-corr206Pb/238U Age\"] )  ", true, false, false);
        meansAndAgesForRefMaterials.add(expression4corr206Pb238UAgeUnct);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr  206/238  *** END
        //
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 7-corr  206/238  *** Start
        Expression expression7corr206Pb238Ucalibrconst = buildExpression("7-corr206Pb/238Ucalibr.const",
                "(1 - [\"204/206 (fr. 207)\"] * sComm_64) * [\"UncorrPb/Uconst\"]", true, true, false);
        meansAndAgesForRefMaterials.add(expression7corr206Pb238Ucalibrconst);

        Expression expression7corr206Pb238Ucalibrconsterr = buildExpression("7-corr206Pb/238Ucalibr.const %err",
                "sqrt([%\"UncorrPb/Uconst\"]^2 +\n"
                + "( sComm_64 / (1 / [\"204/206 (fr. 207)\"] - sComm_64 ) )^2 * \n"
                + "[\"204/206 (fr. 207) %err\"]^2)", true, true, false);
        meansAndAgesForRefMaterials.add(expression7corr206Pb238Ucalibrconsterr);

        // weighted mean
        Expression expression7corr206Pb238UcalibrconstWM = buildExpression("7-corr206Pb/238Ucalibr.const WM",
                "WtdMeanACalc( [\"7-corr206Pb/238Ucalibr.const\"], [\"7-corr206Pb/238Ucalibr.const %err\"], FALSE, FALSE )", true, false, true);
        meansAndAgesForRefMaterials.add(expression7corr206Pb238UcalibrconstWM);

        // age calc
        Expression expression7corr206Pb238UAge = buildExpression("7-corr206Pb/238U Age",
                "LN( 1.0 + [\"7-corr206Pb/238Ucalibr.const\"] / [\"7-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio ) / lambda238", true, false, false);
        meansAndAgesForRefMaterials.add(expression7corr206Pb238UAge);

        Expression expression7corr206Pb238UAgeUnct = buildExpression("7-corr206Pb/238U Age 1sigma",
                "[\"7-corr206Pb/238Ucalibr.const %err\"] / 100 * ( EXP(lambda238 * [\"7-corr206Pb/238U Age\"] ) - 1 ) / lambda238 / EXP(lambda238 * [\"7-corr206Pb/238U Age\"] )  ", true, false, false);
        meansAndAgesForRefMaterials.add(expression7corr206Pb238UAgeUnct);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 7-corr  206/238  *** End
        //   
        return meansAndAgesForRefMaterials;
    }

    /**
     * Squid2.5 Framework: Part 4 means
     *
     * @return
     */
    public static SortedSet<Expression> generate208MeansAndAgesForRefMaterialsU() {
        SortedSet<Expression> meansAndAgesForRefMaterials = new TreeSet<>();

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 8-corr  206/238  *** Start
        Expression expression8corr206Pb238Ucalibrconst = buildExpression("8-corr206Pb/238Ucalibr.const",
                "(1 - [\"204/206 (fr. 208)\"] * sComm_64) * [\"UncorrPb/Uconst\"]", true, true, false);
        meansAndAgesForRefMaterials.add(expression8corr206Pb238Ucalibrconst);

        String term2 = "( sComm_64 * [\"UncorrPb/Uconst\"] * [\"204/206 (fr. 208)\"] / [\"8-corr206Pb/238Ucalibr.const\"] )^2 ";
        String term3 = "( [\"208/206\"] * [%\"208/206\"] / \n"
                + "( [\"208/206\"] - StdRad86fact * [\"232Th/238U\"] ) )^2 ";
        String term4 = "1 / ( [\"208/206\"] - StdRad86fact * [\"232Th/238U\"] ) +  \n"
                + "sComm_64 / ( sComm_84 - sComm_64 * StdRad86fact * [\"232Th/238U\"] )";
        String term6 = "((" + term4 + ")* StdRad86fact * [\"232Th/238U\"] * [%\"232Th/238U\"] )^2";

        Expression expression8corr206Pb238Ucalibrconsterr = buildExpression("8-corr206Pb/238Ucalibr.const %err",
                "sqrt( [%\"UncorrPb/Uconst\"]^2 + ((" + term2 + ") * ((" + term3 + ") + (" + term6 + "))) )", true, true, false);
        meansAndAgesForRefMaterials.add(expression8corr206Pb238Ucalibrconsterr);

        // weighted mean
        Expression expression8corr206Pb238UcalibrconstWM = buildExpression("8-corr206Pb/238Ucalibr.const WM",
                "WtdMeanACalc( [\"8-corr206Pb/238Ucalibr.const\"], [\"8-corr206Pb/238Ucalibr.const %err\"], FALSE, FALSE )", true, false, true);
        meansAndAgesForRefMaterials.add(expression8corr206Pb238UcalibrconstWM);

        // age calc
        Expression expression8corr206Pb238UAge = buildExpression("8-corr206Pb/238U Age",
                "LN( 1.0 + [\"8-corr206Pb/238Ucalibr.const\"] / [\"8-corr206Pb/238Ucalibr.const WM\"][0] * StdUPbRatio ) / lambda238", true, false, false);
        meansAndAgesForRefMaterials.add(expression8corr206Pb238UAge);

        Expression expression8corr206Pb238UAgeUnct = buildExpression("8-corr206Pb/238U Age 1sigma",
                "[\"8-corr206Pb/238Ucalibr.const %err\"] / 100 * ( EXP(lambda238 * [\"8-corr206Pb/238U Age\"] ) - 1 ) / lambda238 / EXP(lambda238 * [\"8-corr206Pb/238U Age\"] )  ", true, false, false);
        meansAndAgesForRefMaterials.add(expression8corr206Pb238UAgeUnct);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 8-corr  206/238  *** End
        return meansAndAgesForRefMaterials;
    }

    /**
     * Squid2.5 Framework: Part 4 means
     *
     * @return
     */
    public static SortedSet<Expression> generate204207MeansAndAgesForRefMaterialsTh() {
        SortedSet<Expression> meansAndAgesForRefMaterials = new TreeSet<>();

        //
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr  208/232  *** Start
        Expression expression4corr208Pb232Thcalibrconst = buildExpression("4-corr208Pb/232Thcalibr.const",
                "(1 - [\"204/206\"] / [\"208/206\"] * sComm_84) * [\"UncorrPb/Thconst\"]", true, true, false);
        meansAndAgesForRefMaterials.add(expression4corr208Pb232Thcalibrconst);

        Expression expression4corr208Pb232Thcalibrconsterr = buildExpression("4-corr208Pb/232Thcalibr.const %err",
                "sqrt([%\"UncorrPb/Thconst\"]^2 + \n"
                + "( sComm_84 / ( [\"208/206\"] / [\"204/206\"] - sComm_84 ) )^2 * [%\"204/206\"]^2 )", true, true, false);
        meansAndAgesForRefMaterials.add(expression4corr208Pb232Thcalibrconsterr);

        // weighted mean
        Expression expression4corr208Pb232ThcalibrconstWM = buildExpression("4-corr208Pb/232Thcalibr.const WM",
                "WtdMeanACalc( [\"4-corr208Pb/232Thcalibr.const\"], [\"4-corr208Pb/232Thcalibr.const %err\"], FALSE, FALSE )", true, false, true);
        meansAndAgesForRefMaterials.add(expression4corr208Pb232ThcalibrconstWM);

        // age calc
        Expression expression4corr208Pb232ThAge = buildExpression("4-corr208Pb/232Th Age",
                "LN( 1.0 + [\"4-corr208Pb/232Thcalibr.const\"] / [\"4-corr208Pb/232Thcalibr.const WM\"][0] * StdThPbRatio ) / lambda232", true, false, false);
        meansAndAgesForRefMaterials.add(expression4corr208Pb232ThAge);

        Expression expression4corr208Pb232ThAgeUnct = buildExpression("4-corr208Pb/232Th Age 1sigma",
                "[\"4-corr208Pb/232Thcalibr.const %err\"] / 100 * ( EXP(lambda232 * [\"4-corr208Pb/232Th Age\"] ) - 1 ) / lambda232 / EXP(lambda232 * [\"4-corr208Pb/232Th Age\"] )  ", true, false, false);
        meansAndAgesForRefMaterials.add(expression4corr208Pb232ThAgeUnct);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 4-corr  208/232  *** END
        //
        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 7-corr  208/232  *** Start
        Expression expression7corr208Pb232Thcalibrconst = buildExpression("7-corr208Pb/232Thcalibr.const",
                "(1 - [\"204/206 (fr. 207)\"] / [\"208/206\"] * sComm_84) * [\"UncorrPb/Thconst\"]", true, true, false);
        meansAndAgesForRefMaterials.add(expression7corr208Pb232Thcalibrconst);

        Expression expression7corr208Pb232Thcalibrconsterr = buildExpression("7-corr208Pb/232Thcalibr.const %err",
                "sqrt([%\"UncorrPb/Thconst\"]^2 +  \n"
                + "( sComm_84 / ( [\"208/206\"] / [\"204/206 (fr. 207)\"] - sComm_84 ) )^2 * \n"
                + "( [%\"208/206\"]^2 + [\"204/206 (fr. 207) %err\"]^2 ))", true, true, false);
        meansAndAgesForRefMaterials.add(expression7corr208Pb232Thcalibrconsterr);

        // weighted mean
        Expression expression7corr208Pb232ThcalibrconstWM = buildExpression("7-corr208Pb/232Thcalibr.const WM",
                "WtdMeanACalc( [\"7-corr208Pb/232Thcalibr.const\"], [\"7-corr208Pb/232Thcalibr.const %err\"], FALSE, FALSE )", true, false, true);
        meansAndAgesForRefMaterials.add(expression7corr208Pb232ThcalibrconstWM);

        // age calc
        Expression expression7corr208Pb232ThAge = buildExpression("7-corr208Pb/232Th Age",
                "LN( 1.0 + [\"7-corr208Pb/232Thcalibr.const\"] / [\"7-corr208Pb/232Thcalibr.const WM\"][0] * StdThPbRatio ) / lambda232", true, false, false);
        meansAndAgesForRefMaterials.add(expression7corr208Pb232ThAge);

        Expression expression7corr208Pb232ThAgeUnct = buildExpression("7-corr208Pb/232Th Age 1sigma",
                "[\"7-corr208Pb/232Thcalibr.const %err\"] / 100 * ( EXP(lambda232 * [\"7-corr208Pb/232Th Age\"] ) - 1 ) / lambda232 / EXP(lambda232 * [\"7-corr208Pb/232Th Age\"] )  ", true, false, false);
        meansAndAgesForRefMaterials.add(expression7corr208Pb232ThAgeUnct);

        // +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ 7-corr  208/232  *** End
        return meansAndAgesForRefMaterials;
    }

    private static Expression buildExpression(String name, String excelExpression, boolean isRefMatCalc, boolean isSampleCalc, boolean isSummaryCalc) {
        Expression expression = new Expression(name, excelExpression);
        expression.setSquidSwitchNU(false);

        ExpressionTreeInterface expressionTree = expression.getExpressionTree();
        expressionTree.setSquidSwitchSTReferenceMaterialCalculation(isRefMatCalc);
        expressionTree.setSquidSwitchSAUnknownCalculation(isSampleCalc);
        expressionTree.setSquidSwitchSCSummaryCalculation(isSummaryCalc);

        expressionTree.setSquidSwitchConcentrationReferenceMaterialCalculation(false);
        expressionTree.setSquidSpecialUPbThExpression(true);
        expressionTree.setRootExpressionTree(true);
//        System.out.println(">>>>>   " + expressionTree.getName());

        return expression;
    }

}
