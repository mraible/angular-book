describe('Home', () => {
  before(() => {
    cy.visit('/')
    cy.signIn(
      Cypress.env('E2E_USERNAME'),
      Cypress.env('E2E_PASSWORD')
    )
  })

  it('Visits the initial project page', () => {
    cy.contains('Welcome to ng-demo!')
    cy.contains('Search')
  })
})
