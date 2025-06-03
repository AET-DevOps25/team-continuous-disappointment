import { useEffect } from "react";
import { useAuth as useOAuth } from "react-oidc-context";
import { useNavigate } from "react-router-dom";

function LandingPage() {
  const auth = useOAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (auth.isAuthenticated) navigate("/");
  }, [auth, navigate]);

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-xl shadow-lg">
        <div className="text-center">
          <h2 className="mt-6 text-3xl font-bold text-gray-900">Welcome</h2>
          <p className="mt-2 text-sm text-gray-600">
            Please sign in with your LRZ account to continue
          </p>
        </div>
        <div className="mt-8">
          <button
            className="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-colors duration-200"
            onClick={() => auth.signinRedirect()}
          >
            <span className="absolute left-0 inset-y-0 flex items-center pl-3">
              <svg
                className="h-5 w-5 text-indigo-500 group-hover:text-indigo-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 24 24"
                fill="currentColor"
              >
                <path d="M20.892 9.889a.664.664 0 0 0-.025-.087l-3.065-7.5A.749.749 0 0 0 17.089 2H6.911a.749.749 0 0 0-.713.302l-3.065 7.5a.673.673 0 0 0-.025.087A7.976 7.976 0 0 0 4 16v.001A7.993 7.993 0 0 0 12 24a7.993 7.993 0 0 0 8-7.999V16a7.976 7.976 0 0 0 .892-6.111ZM8.472 7.485a.25.25 0 0 1-.25-.25V6.251H6.607a.124.124 0 0 1-.116-.173l.399-.959h1.332V4.127a.25.25 0 0 1 .25-.25h1.045a.25.25 0 0 1 .25.25v.992h1.332l.399.959a.124.124 0 0 1-.116.173H9.767v.984a.25.25 0 0 1-.25.25H8.472Z" />
              </svg>
            </span>
            Sign in with GitLab LRZ
          </button>
        </div>
      </div>
    </div>
  );
}

export default LandingPage;
