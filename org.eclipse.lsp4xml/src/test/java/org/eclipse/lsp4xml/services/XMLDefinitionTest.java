/*******************************************************************************
* Copyright (c) 2019 Red Hat Inc. and others.
* All rights reserved. This program and the accompanying materials
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v20.html
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package org.eclipse.lsp4xml.services;

import static org.eclipse.lsp4xml.XMLAssert.ll;
import static org.eclipse.lsp4xml.XMLAssert.r;

import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4xml.XMLAssert;
import org.eclipse.lsp4xml.commons.BadLocationException;
import org.junit.Test;

/**
 * XML definition tests
 * 
 * @author Angelo ZERR
 *
 */
public class XMLDefinitionTest {

	@Test
	public void noStartTagDefinition() throws BadLocationException {
		// definition on |
		String xml = "<?xml version='1.0'?>\r\n" + //
				"<root>\r\n" + //
				"</chi|ld>"; //
		testDefinitionFor(xml);
	}

	@Test
	public void noEndTagDefinition() throws BadLocationException {
		// definition on |
		String xml = "<?xml version='1.0'?>\r\n" + //
				"<roo|t>\r\n" + //
				"</child>"; //
		testDefinitionFor(xml);
	}

	@Test
	public void startTagDefinition() throws BadLocationException {
		// definition on |
		String xml = "<?xml version='1.0'?>\r\n" + //
				"<root>\r\n" + //
				"</ro|ot>"; //
		testDefinitionFor(xml, ll("test.xml", r(2, 2, 2, 6), r(1, 1, 1, 5)));
	}

	@Test
	public void endTagDefinition() throws BadLocationException {
		// definition on |
		String xml = "<?xml version='1.0'?>\r\n" + //
				"<roo|t>\r\n" + //
				"</root>"; //
		testDefinitionFor(xml, ll("test.xml", r(1, 1, 1, 5), r(2, 2, 2, 6)));
	}

	private static void testDefinitionFor(String xml, LocationLink... expectedItems) throws BadLocationException {
		XMLAssert.testDefinitionFor(xml, "test.xml", expectedItems);
	}
}
