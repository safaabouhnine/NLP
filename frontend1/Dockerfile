# ---- Build Stage ----
FROM node:20.13.1 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json first for dependency installation
COPY package*.json ./

# Install dependencies
RUN NODE_ENV=development npm i

# Copy the rest of the application source code (including src and index.html)
COPY . .

# Build the production version of your React app
RUN npm run build

# ---- Run Stage ----
FROM node:20.13.1 AS run

# Install 'serve' globally to serve the built files
RUN npm install -g serve

# Set the working directory
WORKDIR /app

# Copy the built files from the build stage
COPY --from=build /app/build ./build

# Expose the port that 'serve' will run on
EXPOSE 3000

# Start the application using 'serve'
CMD ["serve", "-s", "build", "-l", "3000"]
