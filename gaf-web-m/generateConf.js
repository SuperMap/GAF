const ejs = require('ejs')
const fs = require('fs')

const data = {
  dirs: []
};

function readDir() {
  const dirs = fs.readdirSync('./packages', 'utf-8')
  dirs.forEach(function(dir) {
    const stat = fs.statSync(`./packages/${dir}`)
    if (stat.isDirectory() && !['test', 'portal'].includes(dir)) {
      data.dirs.push(dir);
    }
  })
}

function writeFile() {
  try {
    readDir()
    ejs.renderFile('_default.conf', data, null, function(err, str){
      if (err) {
        console.log(err);
        return;
      }
      fs.writeFileSync('default.conf', str);
    });
  } catch (ex) {
    console.log(ex);
  }
}

writeFile();