package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import snakes.SnakesAndLadders

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SnakesAndLaddersController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  val controller = SnakesAndLadders.controller;

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  
  
  def index() = Action { implicit request: Request[AnyContent] =>
    print(controller.createGame(10))
    Ok(views.html.index())
  }
}
