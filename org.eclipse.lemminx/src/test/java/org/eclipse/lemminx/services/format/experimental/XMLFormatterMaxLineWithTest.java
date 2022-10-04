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

import org.eclipse.lemminx.AbstractCacheBasedTest;
import org.eclipse.lemminx.XMLAssert;
import org.eclipse.lemminx.commons.BadLocationException;
import org.eclipse.lemminx.settings.SharedSettings;
import org.eclipse.lsp4j.TextEdit;
import org.junit.jupiter.api.Test;

/**
 * XML experimental formatter services tests with max line width.
 *
 */
public class XMLFormatterMaxLineWithTest extends AbstractCacheBasedTest {

	@Test
	public void splitText() throws BadLocationException {
		String content = "<a>abcde fghi</a>";
		String expected = "<a>abcde" + //
				System.lineSeparator() + //
				"fghi</a>";
		assertFormat(content, expected, 6, //
				te(0, 8, 0, 9, System.lineSeparator()));
		assertFormat(expected, expected, 6);
	}

	@Test
	public void splitTextLongElementName() throws BadLocationException {
		String content = "<aaaaaa>abcde fghi</aaaaaa>";
		String expected = "<aaaaaa>abcde" + //
				System.lineSeparator() + //
				"fghi</aaaaaa>";
		assertFormat(content, expected, 6, //
				te(0, 13, 0, 14, System.lineSeparator()));
		assertFormat(expected, expected, 6);
	}

	@Test
	public void splitMixedText() throws BadLocationException {
		String content = "<a><b /> efgh</a>";
		String expected = "<a><b />" + //
				System.lineSeparator() + //
				"efgh</a>";
		assertFormat(content, expected, 5, //
				te(0, 8, 0, 9, System.lineSeparator()));
		assertFormat(expected, expected, 5);
	}

	@Test
	public void noSplit() throws BadLocationException {
		String content = "<a>abcde fghi</a>";
		String expected = content;
		assertFormat(content, expected, 20);
	}

	@Test
	public void splitWithAttribute() throws BadLocationException {
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setTabSize(4);
		String content = "<aaaaaaaaa bb=\"tes t\" c=\"a\" d=\"a\" e=\"a\"> </aaaaaaaaa>";
		String expected = "<aaaaaaaaa" + System.lineSeparator() + //
				"    bb=\"tes t\" c=\"a\"" + System.lineSeparator() + //
				"    d=\"a\" e=\"a\"> </aaaaaaaaa>";
		assertFormat(content, expected, settings, 20, //
				te(0, 10, 0, 11, System.lineSeparator() + "    "), //
				te(0, 27, 0, 28, System.lineSeparator() + "    "));
		assertFormat(expected, expected, settings, 20);
	}

	@Test
	public void splitWithAttributeMixedDontSplit() throws BadLocationException {
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setTabSize(4);
		settings.getFormattingSettings().setSpaceBeforeEmptyCloseTag(false);
		String content = "<a bb=\"test\"> <b/> h </a>";
		String expected = "<a" + System.lineSeparator() + //
				"    bb=\"test\">" + System.lineSeparator() + //
				"    <b/> h </a>";
		assertFormat(content, expected, settings, 10, //
		te(0, 2, 0, 3, System.lineSeparator() + "    "), //
		te(0, 13, 0, 14, System.lineSeparator() + "    "));
		assertFormat(expected, expected, settings, 10);
	}

	@Test
	public void splitWithAttributeMixedSplit() throws BadLocationException {
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setTabSize(4);
		settings.getFormattingSettings().setSpaceBeforeEmptyCloseTag(false);
		String content = "<a bb=\"test\"> <b/> gh </a>";
		String expected = "<a" + System.lineSeparator() + //
				"    bb=\"test\">" + System.lineSeparator() + //
				"    <b/>" + System.lineSeparator() + //
				"gh </a>";
		assertFormat(content, expected, settings, 10, //
		te(0, 2, 0, 3, System.lineSeparator() + "    "), //
		te(0, 13, 0, 14, System.lineSeparator() + "    "), //
		te(0, 18, 0, 19, System.lineSeparator()));
		assertFormat(expected, expected, settings, 10);
	}

	@Test
	public void splitWithAttributeNested() throws BadLocationException {
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setTabSize(4);
		String content = "<a bb=\"test\"> <b c=\"test\"> </b>  </a>";
		String expected = "<a" + System.lineSeparator() + //
				"    bb=\"test\">" + System.lineSeparator() + //
				"    <b" + System.lineSeparator() + //
				"        c=\"test\">" + System.lineSeparator() + //
				"    </b>" + System.lineSeparator() + //
				"</a>";
		assertFormat(content, expected, settings, 10, //
				te(0, 10, 0, 11, System.lineSeparator() + "    "), //
				te(0, 27, 0, 28, System.lineSeparator() + "    "));
		assertFormat(expected, expected, settings, 10);
	}

	@Test
	public void splitWithAttributeKeepSameLine() throws BadLocationException {
		SharedSettings settings = new SharedSettings();
		settings.getFormattingSettings().setTabSize(4);
		String content = "<aaaaaaaaa bb=\"t tt\" c=\"a\" d=\"a\" e=\"a\"> </aaaaaaaaa>";
		String expected = "<aaaaaaaaa bb=\"t tt\"" + System.lineSeparator() + //
				"    c=\"a\" d=\"a\"" + System.lineSeparator() + //
				"    e=\"a\"> </aaaaaaaaa>";
		assertFormat(content, expected, settings, 20, //
				te(0, 20, 0, 21, System.lineSeparator() + "    "), //
				te(0, 32, 0, 33, System.lineSeparator() + "    "));
		assertFormat(expected, expected, settings, 20);
	}

	@Test
	public void longText() throws BadLocationException {
		String content = "<foo>\r\n" + //
				"	<para>    \r\n" + //
				"		vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv\r\n"
				+ //
				"	</para>\r\n" + //
				"</foo>";
		String expected = "<foo>\r\n" + //
				"  <para>\r\n" + //
				"vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv </para>\r\n"
				+ //
				"</foo>";
		assertFormat(content, expected, 20, //
				te(0, 5, 1, 1, "\r\n  "), //
				te(1, 7, 2, 2, "\r\n"), //
				te(2, 102, 3, 1, " "));
		assertFormat(expected, expected, 20);
	}

	@Test
	public void complex() throws BadLocationException {
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + //
				"<ip_log  version=\"1.0\">\r\n" + //
				"  <project id=\"org.apache.ant\" version=\"1.10.12\" status=\"done\">\r\n" + //
				"    <info>\r\n" + //
				"      <name>Apache            Ant (all-in-one) ffffffffffffffffff        fffffffffffffffffffffffff    ggggggggggggggg</name>\r\n"
				+ //
				"      <repository>scm:git:git.eclipse.org:/gitroot/orbit/recipes.git</repository>\r\n" + //
				"      <location>apache-parent/ant/org.apache.ant</location>\r\n" + //
				"    </info>\r\n" + //
				"    <contact>\r\n" + //
				"      <name>Sarika          \r\n" + //
				"        Sinha</name>\r\n" + //
				"      <email>sarika.\r\n" + //
				"        \r\n" + //
				"        \r\n" + //
				"        sinha@in.ibm.com</email>\r\n" + //
				"      <company>IBM</company>\r\n" + //
				"    </contact>\r\n" + //
				"  </project>\r\n" + //
				"</ip_log>";

		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + //
				"<ip_log version=\"1.0\">\r\n" + //
				"  <project id=\"org.apache.ant\" version=\"1.10.12\" status=\"done\">\r\n" + //
				"    <info>\r\n" + //
				"      <name>Apache Ant (all-in-one) ffffffffffffffffff fffffffffffffffffffffffff\r\n" + //
				"ggggggggggggggg</name>\r\n" + //
				"      <repository>scm:git:git.eclipse.org:/gitroot/orbit/recipes.git</repository>\r\n" + //
				"      <location>apache-parent/ant/org.apache.ant</location>\r\n" + //
				"    </info>\r\n" + //
				"    <contact>\r\n" + //
				"      <name>Sarika Sinha</name>\r\n" + //
				"      <email>sarika. sinha@in.ibm.com</email>\r\n" + //
				"      <company>IBM</company>\r\n" + //
				"    </contact>\r\n" + //
				"  </project>\r\n" + //
				"</ip_log>";

		assertFormat(content, expected, 80, //
				te(1, 7, 1, 9, " "), //
				te(4, 18, 4, 30, " "), //
				te(4, 65, 4, 73, " "), //
				te(4, 98, 4, 102, "\r\n"), //
				te(9, 18, 10, 8, " "), //
				te(11, 20, 14, 8, " "));
		assertFormat(expected, expected, 80);
	}

	private static void assertFormat(String unformatted, String expected, SharedSettings sharedSettings,
			int maxLineWidth, TextEdit... expectedEdits)
			throws BadLocationException {
		sharedSettings.getFormattingSettings().setMaxLineWidth(maxLineWidth);
		sharedSettings.getFormattingSettings().setJoinContentLines(true);
		// Force to "experimental" formatter
		sharedSettings.getFormattingSettings().setExperimental(true);
		XMLAssert.assertFormat(null, unformatted, expected, sharedSettings, "test.xml", Boolean.FALSE, expectedEdits);
	}

	private static void assertFormat(String unformatted, String expected, int maxLineWidth, TextEdit... expectedEdits)
			throws BadLocationException {
		SharedSettings sharedSettings = new SharedSettings();
		sharedSettings.getFormattingSettings().setMaxLineWidth(maxLineWidth);
		sharedSettings.getFormattingSettings().setJoinContentLines(true);
		// Force to "experimental" formatter
		sharedSettings.getFormattingSettings().setExperimental(true);
		XMLAssert.assertFormat(null, unformatted, expected, sharedSettings, "test.xml", Boolean.FALSE, expectedEdits);
	}
}
