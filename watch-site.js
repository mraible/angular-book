const chokidar = require('chokidar')
const bs = require('browser-sync').create()
const generator = require('@antora/site-generator')
const Lock = require('./lock.js')
const processorLock = new Lock()

const antoraArgs = ['--playbook', 'site.yml']

const watcher = chokidar.watch([
  `src/docs/**`,
], {
  ignored: [
    /(^|[\/\\])\../, // ignore dotfiles
  ],
  persistent: true,
  awaitWriteFinish: {
    stabilityThreshold: 100,
    pollInterval: 50
  }
})

async function generate(_) {
  try {
    const hasQueuedEvents = await processorLock.acquire()
    if (!hasQueuedEvents) {
      await generator(antoraArgs, process.env)
      bs.reload({
        notify: false
      })
    }
  } catch (err) {
    console.error(err)
  } finally {
    processorLock.release()
  }
}

watcher.on('change', async e => await generate(e))
watcher.on('unlink', async e => await generate(e))

;(async () => {
  await generate()
  bs.init({
    notify: false,
    server: './public'
  })
})()
