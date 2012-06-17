package org.hoschi.sweetp.services.testpub

import groovy.util.logging.Log4j
import groovyx.net.http.ContentType
import groovyx.net.http.RESTClient
import org.hoschi.sweetp.services.base.tcp.groovy.TcpService

/**
 * Test service to publish hook.
 *
 * @author Stefan Gojan
 */
@Log4j
class TestPub extends TcpService {
	static void main(String[] args) throws Exception {
		def port = System.getenv('PORT')
		assert port
		assert port.isInteger()

		TestPub own = new TestPub()
		own.connect('localhost', new Integer(port))
		own.listen()
	}

	@Override
	List getConfig(Map params) {
		[
				['/tests/service/testpub/sayhello': [
						method: 'sayhello',
						params: [
								url: 'url',
						],
						description: [
								summary: 'This is just a test for the main server you can call this function which provids a pre and post hook.',
								example: 'sweetp tests service testpub sayhello'
						],
						hooks: [
								pub: ['/testpub/sayhello/pre',
										'/testpub/sayhello/post']
						],
				]]
		]
	}

	/**
	 * Say hello method with hooks.
	 *
	 * @param params contain url
	 * @return what hooks returned and original text
	 */
	Object sayhello(Map params) {
		assert params.url

		// use http is ok here, it should be a call from the same host so
		// insecure communication is ok.
		String url = params.url
		log.info "calling url was: $url"

		String path = '/hooks/noproject/testpub/sayhello/pre'
		log.info "calling path $path"

		RESTClient server = new RESTClient(url)
		def resp = server.post(
				path: path,
				body: [text: 'nothing yet'],
				requestContentType: ContentType.JSON,
				headers: [Accept: 'application/json']
		)

		assert resp.status == 200
		log.info "response from pre hook was: $resp.data"
		def pre = resp.data

		log.info 'service: hello'
		String hello = 'sayhello'

		path = '/hooks/noproject/testpub/sayhello/post'
		log.info "calling path $path"

		resp = server.post(
				path: path,
				body: [text: hello],
				requestContentType: ContentType.JSON,
				headers: [Accept: 'application/json']
		)

		assert resp.status == 200
		log.info "response from post hook was: $resp.data"
		def post = resp.data

		"""pre hook returned text -> $pre.text
post hook returned text -> $post.text
original text -> $hello"""
	}
}
