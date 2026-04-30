import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { resolve } from 'path'

export default defineConfig({
  plugins: [react()],

  resolve: {
    alias: {
      // Alias para imports limpios: import X from '@/compartido/...'
      '@': resolve(__dirname, 'src'),
    },
  },

  server: {
    port: 5173,
    // Proxy para evitar CORS en desarrollo: las llamadas a /api van al backend
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },

  build: {
    outDir: 'dist',
    sourcemap: true,
  },
})
