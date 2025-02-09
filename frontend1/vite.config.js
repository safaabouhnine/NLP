import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  server: {
    hmr: false
  },
  plugins: [react()],
  root: './',  // Définit la racine du projet
  build: {
    outDir: 'build', // Dossier de sortie pour la build
  },
})
