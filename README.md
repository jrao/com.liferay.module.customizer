# Purpose

Goal: This repository contains a template for creating projects that customize Liferay modules. https://github.com/jrao/com.liferay.login.web.customizer is an example of the kind of project that this repository provides the tools to create. The main tool provided by this repository is a project template. This project template contains a Gradle script that automates project setup tasks and allow developers to start customizing right away.

## Steps to Customize a Liferay Module

1. Clone this repository.

2. Open the `com.liferay.module.customizer/build.gradle` file. To enter a name for your customizer module, change the default value of the `customizerModuleName` variable to whatever you'd like your new customizer module to be named.

3. From the `com.liferay.module.customizer` directory, run `./gradlew copyTemplate`.

	Check that the template in the `com.liferay.module.customizer` directory was copied into a directory with the name that you configured in step 2. We'll call this directory the customizer module directory from now on.

4. Navigate to the customizer module directory and edit the `build.gradle` there. Enter values for the `groupId`, `artifactId`, and `version` variables corresponding to the Maven coordinates of the Liferay module you'd like your module to customize. For example, to customize the Liferay login web module, you might enter these values:

		def groupId = "com.liferay"
		def artifactId = "com.liferay.login.web"
		def version = "3.0.0"

	Of course, the exact version to enter depends on the version of the target module that's installed on your target Liferay instance.

5. Since you're attempting to customize a specific Liferay module, you should have a copy of the that module's source code available to inspect. Copy the specific resources you'd like to customize from the target module's source code to your customizer project.

	For example, suppose you want to customize the default view of Liferay's Sign In portlet as well as the MVC render command that's invoked when a user clicks on the *Forgot Password* link of the Sign in portlet. In this case, you'd copy the `login-web/src/main/resources/META-INF/resources/login.jsp` file to `src/main/resources/META-INF/resources/login.jsp` in your customizer module. And you'd also copy `login-web/src/main/java/com.liferay.login.web.internal.portlet.action/ForgotPasswordMVCRenderCommand.java` to `src/main/java/com.liferay.login.web.internal.portlet.action/ForgotPasswordMVCRenderCommand.java` in your customizer module.
	
	In order to make this Java class compile, you need to copy the dependencies of the target module (`com.liferay.login.web`) to your customizer module. But keep in mind that any project dependencies must be converted to actual artifact dependencies.
	
	For example, here are the dependencies copied from the `com.liferay.login.web` module to the customizer module:
	
		compileOnly group: "com.liferay", name: "com.liferay.captcha.api", version: "1.1.2"
		compileOnly group: "com.liferay", name: "com.liferay.captcha.taglib", version: "1.0.6"
		compileOnly group: "com.liferay", name: "com.liferay.frontend.taglib", version: "2.2.15"
		compileOnly group: "com.liferay", name: "com.liferay.osgi.service.tracker.collections", version: "2.0.4"
		compileOnly group: "com.liferay", name: "com.liferay.petra.content", version: "1.0.3"
		compileOnly group: "com.liferay", name: "com.liferay.petra.lang", version: "1.1.2"
		compileOnly group: "com.liferay", name: "com.liferay.petra.string", version: "1.1.0"
		compileOnly group: "com.liferay", name: "com.liferay.portal.upgrade.api", version: "1.0.1"
		compileOnly group: "com.liferay.portal", name: "com.liferay.portal.impl", version: "default"
		compileOnly group: "com.liferay.portal", name: "com.liferay.portal.kernel", version: "default"
		compileOnly group: "com.liferay.portal", name: "com.liferay.util.java", version: "default"
		compileOnly group: "com.liferay.portal", name: "com.liferay.util.taglib", version: "default"
		compileOnly group: "javax.portlet", name: "portlet-api", version: "2.0"
		compileOnly group: "javax.servlet", name: "javax.servlet-api", version: "3.0.1"
		compileOnly group: "javax.servlet.jsp", name: "javax.servlet.jsp-api", version: "2.3.1"
		compileOnly group: "org.osgi", name: "org.osgi.core", version: "5.0.0"
		compileOnly group: "org.osgi", name: "org.osgi.service.component.annotations", version: "1.3.0"
	
	Similarly, if you're customizing a project that needs to compile Soy templates, make sure to copy the target module's `package.json` file to your customizer module. Also note that in order to customize some resources from a target module, other related resources must be copied to your customizer in order for the build to succeed. For example, if you want to customize `View.soy` in the Hello Soy web module, you must copy not only `View.soy` to your customizer module, you must also copy `Header.soy` and `Footer.soy` since they're referenced by `View.soy`.
	
	In general, copy from your target module to your customizer only the resources you intend to customize. But keep in mind that you might need to copy additional resources in your to make your customizer module's build succeed.

6. Make your desired customization to the resources you copied from the target module to your customizer module in step 5.

7. From your customizer module, run `./gradlew buildJar`.
   
   This command initiates a sequence of tasks which downloads the target jar, unzips it, overlays it with your custom resources, and zips it back up. Inspect your customizer module's `build.gradle` file for details.

	In your module's `build` directory, look for a resource with the same name as your target module except with a suffix of `customized.jar` instead of just `.jar`. This artifact is ready for deployment and testing. Some customizations (such as those involving Soy templates) require a Liferay restart in order to take effect.

## Notes

The technique described here to produce a custom module uses a strategy of overlaying custom resources on top of the target module's resources. This technique is most useful for relatively simple customizations that you may need to make to Liferay modules. If you have a complex use case (such as needing to write code which necessitates having to alter the target module's `bnd.bnd` file and thus also the target module's `MANIFEST.MF` file), it's probably simpler for you to copy the target module's source code into a new project and make your customizations there.

Note that customization of `.scss` files is supported. That is, if you customize one or more `.scss` files in your customizer module, they'll be compiled into `.css` files for you which will be included in your module's `.jar` file artifact. This is done by the `buildCSS` task which is an indirect dependency of your customizer module's `buildJar` task.

