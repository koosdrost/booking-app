<template>
  <div class="slots-card">
    <div class="slots-header">
      <span>Available Slots</span>
      <button @click="fetchSlots" :disabled="loading" class="refresh-btn">
        <span v-if="!loading">Search</span>
        <span v-else>Searching...</span>
      </button>
    </div>
    <div class="slots-content">
      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>Searching for available slots...</p>
      </div>
      <div v-else-if="error" class="error-state">
        <div class="error-icon">!</div>
        <p>{{ error }}</p>
        <button @click="fetchSlots" class="retry-btn">Retry</button>
      </div>
      <div v-else-if="slots.length === 0" class="empty-state">
        <div class="empty-icon">📅</div>
        <p>No available slots found</p>
        <p class="hint">Click "Search" to look for reservations</p>
      </div>
      <div v-else class="slots-list">
        <div v-for="(slot, idx) in slots" :key="idx" class="slot-card">
          <div class="slot-info">
            <div class="slot-field">{{ slot.field }}</div>
            <div class="slot-details">
              <span class="slot-day">{{ getDayName(slot.day) }}</span>
              <span class="slot-time">{{ slot.time }}</span>
            </div>
          </div>
          <div class="slot-badge">Available</div>
        </div>
      </div>
      <div v-if="slots.length > 0" class="slots-summary">
        Found {{ slots.length }} available slot{{ slots.length !== 1 ? 's' : '' }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const slots = ref([])
const loading = ref(false)
const error = ref(null)

const fetchSlots = async () => {
  loading.value = true
  error.value = null
  try {
    const res = await fetch('/api/available-slots')
    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`)
    }
    const data = await res.json()
    slots.value = data.slots || []
  } catch (e) {
    error.value = 'Failed to fetch available slots. Please try again.'
    console.error('Error fetching slots:', e)
    slots.value = []
  } finally {
    loading.value = false
  }
}

const getDayName = (day) => {
  const days = {
    'ma': 'Monday',
    'di': 'Tuesday',
    'wo': 'Wednesday',
    'do': 'Thursday',
    'vr': 'Friday',
    'za': 'Saturday',
    'zo': 'Sunday'
  }
  return days[day] || day
}

onMounted(() => {
  // Don't auto-fetch on mount to save resources
  // User can click the Search button
})
</script>

<style scoped>
.slots-card {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 4px 24px 0 rgba(180, 200, 255, 0.10), 0 1.5px 4px 0 rgba(180, 200, 255, 0.08);
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.slots-header {
  position: sticky;
  top: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
  font-weight: 700;
  font-size: 1.2rem;
  padding: 1rem 1.5rem;
  border-radius: 16px 16px 0 0;
  z-index: 2;
  box-shadow: 0 2px 8px rgba(102, 126, 234, 0.2);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.refresh-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.refresh-btn:hover:not(:disabled) {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
}

.refresh-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.slots-content {
  flex: 1;
  overflow-y: auto;
  padding: 1.5rem;
  background: #f7fafd;
}

.loading-state, .empty-state, .error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: #6b7280;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #e5eaf2;
  border-top: 4px solid #667eea;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-icon, .error-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.error-icon {
  width: 60px;
  height: 60px;
  background: #f87171;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 2rem;
}

.error-state p {
  color: #f87171;
  font-weight: 600;
  margin-bottom: 1rem;
}

.retry-btn {
  background: #667eea;
  color: white;
  border: none;
  padding: 0.6rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s ease;
}

.retry-btn:hover {
  background: #5568d3;
  transform: translateY(-2px);
}

.hint {
  color: #9ca3af;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

.slots-list {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
}

.slot-card {
  background: white;
  border-radius: 12px;
  padding: 1.2rem;
  box-shadow: 0 2px 8px rgba(180, 200, 255, 0.1);
  border: 1px solid #e5eaf2;
  transition: all 0.3s ease;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.slot-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(102, 126, 234, 0.2);
  border-color: #667eea;
}

.slot-info {
  flex: 1;
}

.slot-field {
  font-weight: 700;
  font-size: 1.1rem;
  color: #2d3a4a;
  margin-bottom: 0.5rem;
}

.slot-details {
  display: flex;
  gap: 1rem;
  font-size: 0.9rem;
}

.slot-day {
  color: #667eea;
  font-weight: 600;
}

.slot-time {
  color: #6b7280;
  font-weight: 500;
}

.slot-badge {
  background: linear-gradient(135deg, #4ade80 0%, #3b9b63 100%);
  color: white;
  padding: 0.4rem 0.8rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
  white-space: nowrap;
}

.slots-summary {
  margin-top: 1.5rem;
  padding: 1rem;
  background: rgba(102, 126, 234, 0.1);
  border-radius: 8px;
  text-align: center;
  color: #667eea;
  font-weight: 600;
}

@media (max-width: 600px) {
  .slots-list {
    grid-template-columns: 1fr;
  }

  .slots-header {
    font-size: 1rem;
    padding: 0.8rem 1rem;
  }

  .refresh-btn {
    padding: 0.4rem 0.8rem;
    font-size: 0.85rem;
  }
}
</style>
