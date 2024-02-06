/** @type {import('tailwindcss').Config} */

export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    colors: {
      'white': '#ffffff',
      'gray': '#5c5c5c',
      'lightGray': 'rgb(220, 220, 220)',
      'main-color': '#73b2fa',
    },
  },
  plugins: [],
}
