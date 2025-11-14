<template>
  <div class="log-card">
    <div class="log-header">Log Messages</div>
    <div class="log-list-wrapper">
      <ul class="log-list">
        <li v-if="logs.length === 0" class="log-empty">No log messages yet.</li>
        <li v-for="(log, idx) in logs.slice().reverse()" :key="idx" :class="logClass(log)">
          <span class="log-dot"></span>
          {{ log }}
        </li>
      </ul>
      <div class="log-fade"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'

const logs = ref([])
let intervalId = null
const logPanel = ref(null)

const fetchLogs = async () => {
  try {
    const res = await fetch('/api/logs')
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`)
    }
    logs.value = await res.json()
  } catch (e) {
    console.error('Error fetching logs:', e)
    // Don't clear logs on error, keep the last successful fetch
  }
}

const logClass = (log) => {
  if (log.toLowerCase().includes('error') || log.toLowerCase().includes('failed')) return 'log-error'
  if (log.toLowerCase().includes('success')) return 'log-success'
  if (log.toLowerCase().includes('limit')) return 'log-limit'
  return 'log-info'
}

onMounted(() => {
  fetchLogs()
  intervalId = setInterval(fetchLogs, 2000)
})

onUnmounted(() => {
  if (intervalId) clearInterval(intervalId)
})

watch(logs, () => {
  if (logPanel.value) {
    logPanel.value.scrollTop = logPanel.value.scrollHeight
  }
})
</script>

<style scoped>
.log-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px 0 rgba(180, 200, 255, 0.10), 0 1.5px 4px 0 rgba(180, 200, 255, 0.08);
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
  min-width: 0;
  max-width: 100%;
  overflow: hidden;
}
.log-header {
  position: sticky;
  top: 0;
  background: #e3e8f7;
  color: #3b4252;
  font-weight: 700;
  font-size: 1.2rem;
  padding: 1rem 1.5rem;
  border-radius: 16px 16px 0 0;
  z-index: 2;
  box-shadow: 0 2px 8px rgba(180, 200, 255, 0.08);
}
.log-list-wrapper {
  position: relative;
  flex: 1;
  height: 100%;
  overflow-y: auto;
  background: transparent;
  padding: 0 1.5rem 0.5rem 1.5rem;
  box-sizing: border-box;
}
.log-list {
  list-style: none;
  padding: 0;
  margin: 0;
  min-height: 100%;
}
.log-empty {
  color: #b0b8c9;
  font-size: 1.1rem;
  margin-top: 2rem;
}
.log-info {
  color: #6b7280;
}
.log-success {
  color: #4ade80;
  font-weight: 600;
}
.log-error {
  color: #f87171;
  font-weight: 600;
}
.log-limit {
  color: #fbbf24;
  font-weight: 600;
}
li {
  margin-bottom: 0.75rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1.05rem;
  transition: color 0.3s;
  word-break: break-word;
}
.log-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  display: inline-block;
  background: #a5b4fc;
}
.log-success .log-dot {
  background: #4ade80;
}
.log-error .log-dot {
  background: #f87171;
}
.log-limit .log-dot {
  background: #fbbf24;
}
.log-fade {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  height: 60px;
  pointer-events: none;
  background: linear-gradient(to bottom, rgba(247,250,255,0) 0%, #fff 100%);
  border-radius: 0 0 16px 16px;
  z-index: 3;
}
</style> 