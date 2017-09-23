package controllers

import javax.inject._

import models.Lugar
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /* Crear lista de lugares
  * Some: hay contenido, None: no hay contenido*/
  var lugares = List(
    Lugar(1, "Barbosa", Some("Municipio de Barbosa")),
    Lugar(2, "Bello", Some("Municipio de Bello")),
    Lugar(3, "Medellín", Some("Municipio de Medellín")),
    Lugar(3, "Envigado", Some("Municipio de Envigado")),
    Lugar(4, "Tangamandapio", None)
  )

  /* Definir lista de lugares como servicio */
  def listaLugares = Action{
    val json = Json.toJson(lugares)
    Ok(json)
  }

  /* Función para añadir elemeno a la lista */
  def agregarLugar = Action{
    request => val json = request.body.asJson.get

      /*  Validar datos recibidos en formato Json
      * :+ = permite agregar datos a lista */
      json.validate[Lugar] match {
        case success:JsSuccess[Lugar] =>
          lugares = lugares :+ success.get
          Ok(Json.toJson(Map("Response" -> "Lugar ingresado exitosamente")))
        case error:JsError => BadRequest(Json.toJson(Map(
          "Error"->"error"
        )))
      }

  }

  /* Función para eliminar lugar
  * filter: retira lista menos los datos que cumplan con la condición entre paréntesis */
  def eliminarLugar(idLugar:Int) = Action{
    lugares = lugares.filterNot( _.id==idLugar )
    Ok(Json.toJson(Map("Response" -> "Lugar eliminado exitosamente")))
  }

  /* Función para actualizar lugar */
  def actualizarLugar = Action{
    request => val json = request.body.asJson.get

      json.validate[Lugar] match {
        case success:JsSuccess[Lugar] =>
          val nuevoLugar = Lugar(success.get.id, success.get.nombre, success.get.descripcion)
          /* Reemplzar lugar recibido por parámetro (id) */
          lugares = lugares.map(x => if(x.id == success.get.id) nuevoLugar else x)
          Ok(Json.toJson(Map("Response" -> "Lugar actualizado exitosamente")))
        //case JsError(e) => BadRequest(Json.toJson(Map("Error"-> e )))
        case error:JsError => BadRequest(Json.toJson(Map("Error"->"error" )))
      }
  }

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }
}