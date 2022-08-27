
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._
/*1.2*/import play.mvc.Http.Request

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template3[Form[controllers.FormData],Request,play.i18n.Messages,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(form: Form[controllers.FormData])(implicit request: Request, messages: play.i18n.Messages):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*3.1*/("""
"""),_display_(/*4.2*/main("Welcome to Play")/*4.25*/ {_display_(Seq[Any](format.raw/*4.27*/("""

 """),_display_(/*6.3*/helper/*6.9*/.form(action = routes.HomeController.upload, Symbol("enctype") -> "multipart/form-data")/*6.97*/ {_display_(Seq[Any](format.raw/*6.99*/("""
  """),_display_(/*7.4*/helper/*7.10*/.inputFile(form("name"))),format.raw/*7.34*/("""
  """),_display_(/*8.4*/helper/*8.10*/.CSRF.formField),format.raw/*8.25*/("""
  """),format.raw/*9.3*/("""<input type="submit" value="upload file"/>
 """)))}),format.raw/*10.3*/("""
""")))}),format.raw/*11.2*/("""
"""))
      }
    }
  }

  def render(form:Form[controllers.FormData],request:Request,messages:play.i18n.Messages): play.twirl.api.HtmlFormat.Appendable = apply(form)(request,messages)

  def f:((Form[controllers.FormData]) => (Request,play.i18n.Messages) => play.twirl.api.HtmlFormat.Appendable) = (form) => (request,messages) => apply(form)(request,messages)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: 9403fb1815bb2861167eee4538340eb7639ce839
                  MATRIX: 610->1|990->31|1175->123|1202->125|1233->148|1272->150|1301->154|1314->160|1410->248|1449->250|1478->254|1492->260|1536->284|1565->288|1579->294|1614->309|1643->312|1718->357|1750->359
                  LINES: 23->1|28->2|33->3|34->4|34->4|34->4|36->6|36->6|36->6|36->6|37->7|37->7|37->7|38->8|38->8|38->8|39->9|40->10|41->11
                  -- GENERATED --
              */
          