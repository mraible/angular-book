// Plugins enable you to tap into, modify, or extend the internal behavior of Cypress
// For more info, visit https://on.cypress.io/plugins-api
const dotenvPlugin = require('cypress-dotenv');

module.exports = (on, config) => {
  config = dotenvPlugin(config)
  return config
}
