/**
 * Created by Administrator on 2018/9/18.
 */
function f1() {
    document.getElementById("btn1").setAttribute("value", "OKKKKK")
    var x = document.getElementById("btn1").attributes[0]
    alert(x)
}