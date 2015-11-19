package org.codehaus.mojo.sqlj;

import org.apache.maven.it.Verifier;
import org.codehaus.plexus.PlexusTestCase;
import org.junit.Test;

/**
 * Unit test for {@link SqljMojo}.
 * 
 * @author finx
 * @since Nov 11, 2015
 */
public class SqljGetVersionTest extends PlexusTestCase {

	/**
	 * Verifies if is possible to retrieve the SQLJ translator's version using
	 * maven's log.
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	@Test
	public void testGetVersion() throws Exception {
		Verifier verifier = new Verifier(getBasedir() + "/target/test-classes/sqlj-getversion/");
		verifier.executeGoal("sqlj:sqlj");
		verifier.verifyErrorFreeLog();
		verifier.verifyTextInLog("Using SQLJ Translator version");
	}

}
