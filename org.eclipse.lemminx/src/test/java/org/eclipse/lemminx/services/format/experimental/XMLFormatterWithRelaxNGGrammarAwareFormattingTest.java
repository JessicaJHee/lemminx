/*******************************************************************************
* Copyright (c) 2022 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lemminx.services.format.experimental;

import static org.eclipse.lemminx.XMLAssert.te;
import static org.junit.jupiter.api.Assertions.fail;

import org.eclipse.lemminx.XMLAssert;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.TextEdit;
import org.junit.jupiter.api.Test;

/**
 * XML experimental formatter services tests with grammar aware formatting with
 * xml bound to RelaxNG.
 *
 */
public class XMLFormatterWithRelaxNGGrammarAwareFormattingTest {
	@Test
	public void testRelaxNGForMixedElement() throws Exception {
		String content = "<?xml-model href=\"mixed-element.rng\"?>\r\n" + //
				"<mixedElement> text \r\n" + // <-- mixedElement is defined as mixed type, should join content
				"   content   </mixedElement>\r\n" + //
				"<notMixed> text\r\n" + // <-- notMixed is NOT defined as mixed type, should NOT join content
				"  content </notMixed>\r\n";
		String expected = "<?xml-model href=\"relaxng/mixed-element.rng\"?>\r\n" + //
				"<mixedElement> text content </mixedElement>\r\n" + //
				"<notMixed> text\r\n" + //
				"  content </notMixed>";
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setMaxLineWidth(0);
		settings.getFormattingSettings().setGrammarAwareFormatting(true);
		assertFormat(content, expected, settings, //
				te(1, 19, 2, 3, " "), //
				te(2, 10, 2, 13, " "), //
				te(4, 21, 5, 0, ""));
		assertFormat(expected, expected, settings);
	}

	@Test
	public void testRelaxNGForEmptyMixedElement() throws Exception {
		String content = "<?xml-model href=\"relaxng/mixed-element.rng\"?>\r\n"
				+ "<mixedElement></mixedElement>";
		String expected = content;
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setMaxLineWidth(0);
		settings.getFormattingSettings().setGrammarAwareFormatting(true);
		try {
			assertFormat(content, expected, settings);
			assertFormat(expected, expected, settings);
		} catch (Exception ex) {
			fail("Formatter failed to process text", ex);
		}
	}

	private static void assertFormat(String unformatted, String expected, SharedSettings sharedSettings,
			TextEdit... expectedEdits) throws BadLocationException {
		assertFormat(unformatted, expected, sharedSettings, "src/test/resources/relaxng/test.xml", true, expectedEdits);
	}

	private static void assertFormat(String unformatted, String expected, SharedSettings sharedSettings, String uri,
			Boolean considerRangeFormat, TextEdit... expectedEdits) throws BadLocationException {
		// Force to "experimental" formatter
		sharedSettings.getFormattingSettings().setExperimental(true);
		XMLAssert.assertFormat(null, unformatted, expected, sharedSettings, uri, true, expectedEdits);
	}
}