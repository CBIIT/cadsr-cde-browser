module.exports = {
  entry: {
    "angular.min":'./node_modules/angular/angular.min.js',
    "angular-animate.min":'./node_modules/angular-animate/angular-animate.min.js'
    "angular-aria":"./node_modules/angular-aria/angular-aria.min.js",
    "angular-cookies:"./node_modules/angular-cookies/angular-cookies.min.js",
    "angular-i18n":"./node_modules/angular-i18n/angular-i18n.min.js",
    "angular-message-format":"./node_modules/angular-message-format/angular-message-format.min.js",
    "angular-messages":"./node_modules/angular-messages/angular-messages.min.js",
    "angular-parse-ext":"./node_modules/angular-parse-ext/angular-parse-ext.min.js",
    "angular-resource":"./node_modules/angular-resource/angular-resource.min.js",
    "angular-route":"./node_modules/angular-route/angular-route.min.js",
    "angular-sanitize":"./node_modules/angular-sanitize/angular-sanitize.min.js",
    "angular-touch":"./node_modules/angular-touch/angular-touch.min.js",
    "angular-mocks":"./node_modules/angular-mocks/angular-mocks.min.js"
  },
  mode: 'production',
  output: {
	path: `${__dirname}/libs/`,
	filename: '[name].js',
  },
};
