<script setup>
import LogPanel from './components/LogPanel.vue'
import AvailableSlots from './components/AvailableSlots.vue'
</script>

<template>
  <div class="main-bg">
    <header class="header">
      <h1>BVCG Reservation Dashboard</h1>
    </header>
    <main class="main-content">
      <section class="panel log-panel">
        <LogPanel />
      </section>
      <section class="panel slots-panel">
        <AvailableSlots />
      </section>
    </main>
    <footer class="footer">
      <span>Made with ❤️ by Koos</span>
    </footer>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;700&display=swap');

body, html, #app, .main-bg {
  height: 100%;
  margin: 0;
  font-family: 'Inter', Arial, sans-serif;
  background: #f7fafd;
}
.main-bg {
  min-height: 100vh;
  width: 100vw;
  display: flex;
  flex-direction: column;
}
.header {
  position: sticky;
  top: 0;
  z-index: 10;
  background: #fff;
  color: #2d3a4a;
  padding: 1.2rem 2rem;
  text-align: center;
  font-size: 2rem;
  font-weight: 700;
  letter-spacing: 1px;
  box-shadow: 0 2px 8px rgba(180, 200, 255, 0.06);
  border-bottom: 1px solid #e5eaf2;
}
.logo {
  font-size: 2rem;
  margin-right: 0.5rem;
  vertical-align: middle;
}
.main-content {
  display: flex;
  flex: 1;
  min-height: 0;
  width: 100vw;
  background: #f7fafd;
  gap: 2rem;
  padding: 2rem 2vw 2rem 2vw;
  box-sizing: border-box;
}
.panel {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 4px 24px 0 rgba(180, 200, 255, 0.10), 0 1.5px 4px 0 rgba(180, 200, 255, 0.08);
  padding: 2rem 1.5rem;
  display: flex;
  flex-direction: column;
  flex: 1 1 0;
  min-width: 0;
  min-height: 60vh;
  max-width: 600px;
  margin: 0 auto;
}
.log-panel {
  margin-right: 0;
}
.slots-panel {
  margin-left: 0;
}
.footer {
  background: #fff;
  color: #2d3a4a;
  text-align: center;
  padding: 0.75rem 0;
  font-size: 1rem;
  letter-spacing: 0.5px;
  border-top: 1px solid #e5eaf2;
  box-shadow: 0 -2px 8px rgba(180, 200, 255, 0.04);
}
@media (max-width: 900px) {
  .main-content {
    flex-direction: column;
    gap: 1.5rem;
    padding: 1rem 0.5rem;
  }
  .panel {
    max-width: 100vw;
    min-height: 40vh;
    padding: 1.2rem 0.5rem;
  }
}
</style> 