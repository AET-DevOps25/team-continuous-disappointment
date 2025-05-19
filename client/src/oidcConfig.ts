export const oidcConfig = {
  authority: "https://gitlab.lrz.de",
  response_type: "code",
  scope: "read_user",
  onSigninCallback: () => {
    const url = new URL(window.location.href);
    url.searchParams.delete("code");
    url.searchParams.delete("state");
    window.history.replaceState({}, document.title, url.pathname + url.search);
  },
  redirect_uri: import.meta.env.VITE_REDIRECT_URI,
  client_id: import.meta.env.VITE_CLIENT_ID,
};
