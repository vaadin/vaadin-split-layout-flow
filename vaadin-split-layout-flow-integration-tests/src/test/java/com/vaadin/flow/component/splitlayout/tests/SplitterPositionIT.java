/*
 * Copyright 2000-2019 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.splitlayout.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vaadin.flow.component.html.testbench.NativeButtonElement;
import com.vaadin.flow.component.html.testbench.SpanElement;
import com.vaadin.flow.component.splitlayout.demo.SplitLayoutView;
import com.vaadin.flow.component.splitlayout.test.SplitterPositionView;
import com.vaadin.flow.component.splitlayout.testbench.SplitLayoutElement;
import com.vaadin.flow.testutil.AbstractComponentIT;
import com.vaadin.flow.testutil.TestPath;
import com.vaadin.testbench.TestBenchElement;

/**
 * Integration tests for {@link SplitLayoutView}.
 */
@TestPath("splitter-position")
public class SplitterPositionIT extends AbstractComponentIT {

    @Before
    public void setUp() {
        open();
    }

    @Test
    public void testSplitterPositionJava() {
        testSplitterPosition("JavaApi");
    }

    @Test
    public void testSplitterPositionElement() {
        testSplitterPosition("ElementApi");
    }

    private void testSplitterPosition(String testId) {
        $(NativeButtonElement.class).id("createLayout" + testId).click();
        SplitLayoutElement layout = $(SplitLayoutElement.class)
                .id("splitLayout" + testId);
        TestBenchElement primaryElement = layout.$(SpanElement.class)
                .id("primary" + testId);
        TestBenchElement secondaryElement = layout.$(SpanElement.class)
                .id("secondary" + testId);
        assertElementWidth(primaryElement,
                 SplitterPositionView.INITIAL_POSITION);
        assertElementWidth(secondaryElement,
                 SplitterPositionView.FINAL_POSITION);

        $(NativeButtonElement.class).id("setSplitPosition" + testId).click();
        assertElementWidth(primaryElement,
                 SplitterPositionView.FINAL_POSITION);
        assertElementWidth(secondaryElement,
                 SplitterPositionView.INITIAL_POSITION);
    }

    private void assertElementWidth(TestBenchElement element, double expected) {
        final double parentWidth = getWidth(element,".parentNode");
        final double splitterWidth = getWidth(element,".parentNode.$.splitter");
        final double calculatedExpectedWidth = expected * (parentWidth - splitterWidth) / 100d;
        final double width = getWidth(element,"");
        final double tolerance = 0.01; // 1% tolerance
        Assert.assertTrue(Math.abs(calculatedExpectedWidth - width) < (calculatedExpectedWidth * tolerance));
    }

    private double getWidth(TestBenchElement element, String path) {
        final String sourcePath = "arguments[0]" + path;

        return ((Number) executeScript("return " + sourcePath + ".getBoundingClientRect()['width']", element)).doubleValue();
    }
}
