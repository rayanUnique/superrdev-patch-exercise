# NOTES

## Summary of changes

**Backend**
- `TaskController.java`: removed an unconditional `Thread.sleep()` that blocked a
  Tomcat request thread on every API call; invalid `status` now returns 400
  instead of crashing with a 500; added bounds checks for `page` and `pageSize`
  so negative/zero values no longer cause `IndexOutOfBoundsException`; replaced
  in-memory `subList` pagination with Spring Data `PageRequest` so `total` is the
  real filtered count and the DB does the pagination; replaced `System.out.println`
  with SLF4J logging.
- `TaskRepository.java`: fixed an SQL operator-precedence bug — without
  parentheses, `AND` binds tighter than `OR`, so the `status` filter only applied
  to the description branch and was effectively ignored for every title match;
  converted the native query to JPQL so `Page<Task>` works with an automatic
  count query; changed `ORDER BY created_at DESC` to `id ASC` to fix the
  inverted ordering shown in the UI.

**Frontend**
- `api.js`: accepted and forwarded an `AbortController` signal to `fetch`;
  removed a leftover `console.log`.
- `hooks/useTasks.js`: created an `AbortController` per request and aborted the
  previous one on cleanup, so stale responses can no longer overwrite fresh
  data; cleared `error` at the start of each request and set `loading=false`
  on failure so the spinner cannot hang.
- `App.jsx`: reset `page` to 1 when the search query or status changes.

## What I chose not to change
- `spring.jpa.open-in-view` warning — Spring Boot default, not a bug.
- Debouncing the search input — would reduce request volume but was outside the
  timebox; recorded as a future improvement.
- Escaping literal `%` / `_` in user search input — rare edge case.

## Biggest remaining risk
`db/oracle/` mirrors the Java query logic but was not fully audited. If it drives
production reporting, it may carry the same operator-precedence defect.

## Tools / AI used
Used Claude to help read the codebase and reason about the SQL precedence bug
and the React race condition. Every change was verified by re-running curl
requests and exercising the UI before committing. Handwritten explanations
were written by me.