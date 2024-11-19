package controllers

import org.apache.pekko.actor.{Actor, ActorRef, ActorSystem, Props}
import org.apache.pekko.stream.Materializer

import javax.inject.*
import play.api.*
import play.api.mvc.*

import scala.math.*
import play.api.libs.json.{JsObject, Json}
import play.api.libs.streams.ActorFlow
import snakes.SnakesAndLadders
import snakes.controller.ControllerInterface
import snakes.util.Event.*
import snakes.util.{Event, Observer}

import scala.swing.Reactor

@Singleton
class SnakesAndLaddersController @Inject()(val controllerComponents: ControllerComponents)
                                          (implicit system: ActorSystem, mat: Materializer) extends BaseController {

  val controller: ControllerInterface = SnakesAndLadders.controller;

  def createGame(size: Int): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.createGame(size)
    NoContent
  }

  def startGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.startGame()
    NoContent
  }

  def restartGame(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.restartGame()
    NoContent
  }

  def addPlayer(playerName: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.addPlayer(playerName)
    NoContent
  }

  def rollDice(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.rollDice()
    NoContent
  }

  def undoLastStep(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    controller.undoLastAction()
    NoContent
  }

  def indexPage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val players = controller.getCurrentGameState.getPlayers.toList // Convert Queue to List
    Ok(views.html.index(players))
  }

  def gamePage(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val players = controller.getCurrentGameState.getPlayers.toList // Convert Queue to List
    val currentPlayer = controller.getCurrentGameState.getCurrentPlayer()
    val boardSize = sqrt(controller.getBoardSize).toInt
    val rolledValue = controller.getCurrentGameState.getCurrentPlayer().getLastRoll
    Ok(views.html.game(boardSize, players, currentPlayer.getName, rolledValue))
  }

  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      println("New connection was established")
      SnakesAndLaddersSocketActorFactory.create(out)
    }
  }

  private def getBoardJson: JsObject = {
    val players = controller.getCurrentGameState.getPlayers.toList
    val currentPlayer = controller.getCurrentGameState.getCurrentPlayer()
    val boardSize = sqrt(controller.getBoardSize).toInt
    val rolledValue = currentPlayer.getLastRoll
    val boardHtml = views.html.game(boardSize, players, currentPlayer.getName, rolledValue).toString
    Json.obj(
      "type" -> "htmlContent",
      "boardHtml" -> boardHtml
    )
  }

  private object SnakesAndLaddersSocketActorFactory {
    def create(out: ActorRef): Props = {
      Props(new SnakesAndLaddersSocketActor(out))
    }
  }

  private class SnakesAndLaddersSocketActor(out: ActorRef) extends Actor with Reactor {

    listenTo(controller)

    def receive: Receive = {
      case msg: String => println(s"Received message: $msg")
    }

    reactions += {
      case Create => out ! Json.obj(
        "type" -> "redirect",
        "location" -> "/"
      ).toString
      case AddPlayer => out ! Json.obj(
        "type" -> "redirect",
        "location" -> "/"
      ).toString
      case Roll(rollResult) => out ! getBoardJson.toString
      case Undo => out ! getBoardJson.toString
      case Update => out ! getBoardJson.toString
      case Start => out ! Json.obj(
        "type" -> "redirect",
        "location" -> "/game"
      ).toString
      case Restart => out ! Json.obj(
        "type" -> "redirect",
        "location" -> "/"
      ).toString
    }
  }
}



