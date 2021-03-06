package omnibus.http.route

import akka.pattern._
import akka.actor._

import spray.json._
import spray.httpx.SprayJsonSupport._
import spray.httpx.encoding._
import spray.routing._
import spray.routing.authentication._
import spray.json._
import spray.httpx.marshalling._
import spray.http._
import HttpHeaders._
import MediaTypes._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import scala.util._

import DefaultJsonProtocol._
import reflect.ClassTag

import omnibus.http.JsonSupport._
import omnibus.http.streaming._
import omnibus.configuration._
import omnibus.domain._
import omnibus.domain.topic._
import omnibus.domain.subscriber._
import omnibus.repository._

class AdminRoute(topicRepo : ActorRef, subRepo : ActorRef) (implicit context: ActorContext) extends Directives {

  implicit def executionContext = context.dispatcher
  implicit def system = context.system
  implicit val timeout = akka.util.Timeout(Settings(system).Timeout.Ask)

  val log: Logger = LoggerFactory.getLogger("omnibus.route.admin")

  val route =
    pathPrefix("admin") {
      authenticate(BasicAuth(Security.adminPassAuthenticator _, realm = "secure site")) { userName =>
        path("topics" / Rest) { topic =>
          validate(!topic.isEmpty, "topic name cannot be empty \n") {
            val topicPath = TopicPath(topic)
            val prettyTopic = topicPath.prettyStr()
            delete { ctx =>
              val futureDel = (topicRepo ? TopicRepositoryProtocol.DeleteTopic(topicPath)).mapTo[Boolean]
              futureDel.onComplete {
                case Success(ok) => ctx.complete(StatusCodes.Accepted, s"Topic $prettyTopic deleted\n")
                case Failure(ex) => ctx.complete(ex)
              }
            }
          }
        } ~ 
        path("subscribers" / Rest) { sub =>
          get {
            complete {
              if (sub.isEmpty) (subRepo ? SubscriberRepositoryProtocol.AllSubs).mapTo[List[SubscriberView]]
              else (subRepo ? SubscriberRepositoryProtocol.SubById(sub)).mapTo[SubscriberView]
            }
          } ~ 
          delete { ctx =>
              val futureDel = (subRepo ? SubscriberRepositoryProtocol.KillSub(sub)).mapTo[Boolean]
              futureDel.onComplete {
                case Success(ok) => ctx.complete(StatusCodes.Accepted, s"Subscriber $sub deleted\n")
                case Failure(ex) => ctx.complete(ex)
              }
            } 
        }  
      }
    }  
}