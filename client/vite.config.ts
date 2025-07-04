import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { readFileSync } from "fs";

export default defineConfig({
  plugins: [
    react(),
    {
      name: "serve-config",
      configureServer(server) {
        server.middlewares.use("/config.json", (req, res) => {
          const config = readFileSync("./config.json", "utf-8");
          res.setHeader("Content-Type", "application/json");
          res.end(config);
        });
      },
    },
  ],
  optimizeDeps: {
    exclude: ["lucide-react"],
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
