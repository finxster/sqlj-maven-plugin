package org.codehaus.mojo.sqlj;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Test;

/**
 * Unit test for {@link SqljMojo}.
 * 
 * @author finx
 * @since Nov 11, 2015
 */
public class SqljMojoTest extends PlexusTestCase {

	/**
	 * Verifies if is possible to retrieve the SQLJ translator's version.
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	@Test
	public void testGetVersion() throws Exception {
		SqljMojo sqljMojo = new SqljMojo();
		MockSystemStreamLog log = new MockSystemStreamLog();
		sqljMojo.setLog(log);

		MavenProject mavenProject = getMavenProject();

		ReflectionUtils.setVariableValueInObject(sqljMojo, "mavenProject", mavenProject);
		ReflectionUtils.setVariableValueInObject(sqljMojo, "sqljDirs", new File[] {});
		ReflectionUtils.setVariableValueInObject(sqljMojo, "sqljFiles", new File[] {});
		ReflectionUtils.setVariableValueInObject(sqljMojo, "generatedResourcesDirectory",
				new File(getBasedir() + "/generated-resources/sqlj"));
		ReflectionUtils.setVariableValueInObject(sqljMojo, "generatedSourcesDirectory",
				new File(getBasedir() + "/generated-sources/sqlj"));

		sqljMojo.execute();

		assertEquals("Must be 2 infos in log", 2, log.getInfos().size());
		assertEquals("Using SQLJ Translator version 'IBM SQLJ 3.69.24'", log.getInfos().get(0).toString());
		assertEquals("No updated SQLJ files found - skipping SQLJ translation.", log.getInfos().get(1).toString());
	}

	private MavenProject getMavenProject() {
		Build build = new Build();
		build.setOutputDirectory("target");

		Model model = new Model();
		model.setBuild(build);

		MavenProject mavenProject = new MavenProject(model);

		Set<Artifact> artifacts = buildClasspath();
		mavenProject.setArtifacts(artifacts);

		return mavenProject;
	}

	private Set<Artifact> buildClasspath() {
		String groupId = "com.ibm.db2";
		String artifactId = "sqlj";
		VersionRange version = VersionRange.createFromVersion("1.0");
		String scope = "compile";
		String type = "jar";
		String classifier = "";

		Artifact sqljDb2Jar = new DefaultArtifact(groupId, artifactId, version, scope, type, classifier,
				new MockArtifactHandler());
		sqljDb2Jar.setFile(
				new File(System.getProperty("user.home") + "/.m2/repository/com/ibm/db2/sqlj/1.0/sqlj-1.0.jar"));

		return Collections.singleton(sqljDb2Jar);
	}

	/**
	 * {@link DefaultArtifactHandler} that is always on classpath.
	 * @author finx
	 * @since Nov 18, 2015
	 *
	 */
	private class MockArtifactHandler extends DefaultArtifactHandler {

		@Override
		public boolean isAddedToClasspath() {
			// always on classpath
			return true;
		}

	}

	/**
	 * {@link SystemStreamLog} used for retrieve logs of plugin's execution.
	 * 
	 * @author finx
	 * @since Nov 18, 2015
	 *
	 */
	private class MockSystemStreamLog extends SystemStreamLog {

		private List<CharSequence> infos = new LinkedList<CharSequence>();

		@Override
		public void info(CharSequence content) {
			super.info(content);

			infos.add(content);
		}

		public List<CharSequence> getInfos() {
			return infos;
		}

	}

}
