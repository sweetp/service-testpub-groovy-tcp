This service shows you how hooks work in sweetp with a TCP service.
You need both services
[pub](https://github.com/sweetp/service-testpub-groovy-tcp)
and
[sub](https://github.com/sweetp/service-testsub-groovy-tcp)
to test it with sweetp.
Put the jar files in your "server/services-tcp" folder and add this
to your "server/services.json" file:

	{
		"id":"pub",
		"exec":[
			"java",
			"-jar",
			"testpub-groovy-tcp-0.1.jar"
		],
		"dir":"services-tcp/"
	},
	{
		"id":"sub",
		"exec":[
			"java",
			"-jar",
			"testsub-groovy-tcp-0.1.jar"
		],
		"dir":"services-tcp/"
	},

After the server started you can test it with this command:

    sweetp -p none tests service testpub sayhello

and get following output:

    pre hook returned text -> original text was: nothing yet
    post hook returned text -> original text was: sayhello
    original text -> sayhello

The service uses a [base class](https://github.com/sweetp/base-groovy-tcp)
for TCP communication, look at that
project for more information about TCP services.
Since groovy lives in the JVM it is recommended to build a jvm service.

For basic information see
[boilerplate service](https://github.com/sweetp/service-boilerplate-groovy).

More Information on [sweet productivity](http://sweet-productivity.com).
