doctype(html)
html(class="no-js",lang="en"){
	head {
		meta(charset="utf-8")
		meta(http-equiv="x-ua-compatible",content="ie=edge")
		title
		meta(name="description",content="")
		meta(name="viewport",content="width=device-width, initial-scale=1")
		link(rel="apple-touch-icon",href="apple-touch-icon.png")
		link(rel="stylesheet",href="css/normalize.css")
		link(rel="stylesheet",href="css/main.css")
		script(src="js/vendor/modernizr-2.8.3.min.js")
	}
	body {
		comment{
			"[if lt IE 8]>"
			p(class="browserupgrade"){
				"You are using an <strong>outdated</strong> browser. "
				"Please <a href='http://browsehappy.com/'>upgrade your browser</a> "
				"to improve your experience."
			}
			"<![endif]"
		}
		comment "Add your site or application content here"
		p "Hello world! This is HTML5 Boilerplate."
		script(src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js")
		script {
			"window.jQuery || document.write('<script src=\"js/vendor/jquery-1.11.3.min.js\"><\/script>')"
		}
		script(src="js/plugins.js")
		script(src="js/main.js")
		script {
			"model.googleAnalytics"
		}
	}
}