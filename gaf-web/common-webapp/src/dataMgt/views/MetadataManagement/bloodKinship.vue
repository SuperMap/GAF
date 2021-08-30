<template>
  <div>
    <canvas id="myCanvas" height="600px" width="1200px"></canvas>
    <svg id="svg"></svg>
  </div>
</template>

<script>
export default {
  data() {
    return {
      svg: document.getElementById('svg'),
    }
  },
  mounted() {
    // this.init()
  },
  methods: {
    init() {
      const mycanvas = document.getElementById('myCanvas');
      const ctx = mycanvas.getContext("2d");
      this.Can(ctx,1,"#ccc",10,10,120,50,"#000","#329DFF","12pt Arial","流程1",40,40);
      this.Can(ctx,1,"#ccc",280,10,120,50,"#000","#329DFF","12pt Arial","流程2",316,40);
      this.Can(ctx,1,"#ccc",550,10,120,50,"#000","#329DFF","12pt Arial","流程3",570,40);
      this.Can(ctx,1,"#ccc",820,10,120,50,"#000","#329DFF","12pt Arial","流程4",855,40);

 
      //   //canvas环境,线X轴起点,线Y轴起点,旋转角度x,y,尖角,尖角,颜色
      // this.drawArrow(ctx,130,30,170,30,0,0,1,'green');
      // this.drawArrow(ctx,130,42,170,42,0,0,1,'blue');
      // this.drawArrow(ctx,240,30,280,30,30,10,1,'green');
      // this.drawArrow(ctx,240,42,280,42,30,10,1,'blue');
      // //字
      // this.Fon(ctx,"12pt Arial","李大钊",180,30,"green");
      // this.Fon(ctx,"12pt Arial","陈独秀",180,60,'red');
    },
    Can(ctx,Bolds,BorderColor,DistanceX,DistanceY,Widths,Hights,Bcolor,FontColor,FontS,Texts,textX,textY){
              //边框背景
        ctx.beginPath();
        ctx.lineWidth=Bolds;
        ctx.strokeStyle=BorderColor;//边框颜色
        ctx.rect(DistanceX,DistanceY,Widths,Hights);
        ctx.fillStyle=FontColor;//背景颜色
        ctx.fill();
        ctx.fillStyle=Bcolor;//字体颜色
        ctx.font = FontS;
        ctx.fillText(Texts, textX, textY);
        ctx.stroke();
        ctx.closePath();
        ctx.moveTo(DistanceX+Widths, DistanceY+Hights/2)
        ctx.lineTo(DistanceX+Widths+270, DistanceY+Hights/2)
        ctx.stroke()
    },
           //字体
    Fon(ctx,FontS,Texts,textX,textY,color){
      ctx.beginPath();
      ctx.font = FontS;
      ctx.fillStyle=color;
      ctx.fillText(Texts, textX, textY);
      ctx.stroke();
      ctx.closePath();
    },
    //箭头
    drawArrow(ctx,fromX,fromY,toX,toY,theta,headlen,width,color) {
        theta = typeof(theta) !== 'undefined' ? theta : 30;
        headlen = typeof(headlen) !== 'undefined' ? headlen : 10;
        width = typeof(width) !== 'undefined' ? width : 1;
        color = typeof(color) !== 'undefined' ? color : '#000'; // 计算各角度和对应的P2,P3坐标
        var angle = Math.atan2(fromY - toY, fromX - toX) * 180 / Math.PI,
            angle1 = (angle + theta) * Math.PI / 180,
            angle2 = (angle - theta) * Math.PI / 180,
            topX = headlen * Math.cos(angle1),
            topY = headlen * Math.sin(angle1),
            botX = headlen * Math.cos(angle2),
            botY = headlen * Math.sin(angle2);
            ctx.save();
            ctx.beginPath();
            var arrowX = fromX - topX;
            var arrowY = fromY - topY;
            ctx.moveTo(arrowX, arrowY);
            ctx.moveTo(fromX, fromY);
            ctx.lineTo(toX, toY);
            arrowX = toX + topX;
            arrowY = toY + topY;
            ctx.moveTo(arrowX, arrowY);
            ctx.lineTo(toX, toY);
            arrowX = toX + botX;
            arrowY = toY + botY;
            ctx.lineTo(arrowX, arrowY);
            ctx.strokeStyle = color;
            ctx.lineWidth = width;
            ctx.stroke();
            ctx.restore();
        },
    // CreateLine(data) {
    //   let g = document.createElementNS("http://www.w3.org/2000/svg","g");
    //   let pathLine = document.createElementNS("http://www.w3.org/2000/svg","path");
    //   pathLine.setAttribute("d","M"+v.M+" 65 L"+v.L+" 65");
    //   g.appendChild(pathLine);
    //   this.svg.appendChild(g);
    // },
    CreateNode() {}
  },

}
</script>

<style>

</style>