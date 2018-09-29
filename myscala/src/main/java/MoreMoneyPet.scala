/**
  * Created by Administrator on 2018/9/29.
  */
trait MoreMoneyPet {
    //约束了当前的接口可以由哪些类来实现
    this:BigDog=>
    def meng()
}
