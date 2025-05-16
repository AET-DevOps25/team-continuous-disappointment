import './App.css'
import HeroSection from './components/HeroSection';
import HowItWorks from './components/HowItWorks';
import Features from './components/Features';
import About from './components/About';
import Contact from './components/Contact';
import Navigation from './components/Navigation';
import ChatPage from './components/ChatPage';
import Footer from './components/Footer';

function App() {
  return (
    <>
      <Navigation />
      <HeroSection />
      <HowItWorks />
      <section id="try-it-out" className="try-it-out-section">
         <h2>Try It Out</h2>
         <p>Input your ingredients and preferences to get a real-time AI-generated recipe.</p>
        <ChatPage />
      </section>
      <Features />
      <About />
      <Contact />
      <Footer />
    </>
  )
}

export default App
