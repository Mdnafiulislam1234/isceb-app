import test from "node:test";
import assert from "node:assert/strict";
import {
  AVAILABLE_USERS,
  DEFAULT_DEVELOPER,
  DEFAULT_FOUNDER,
  FIREBASE_CONFIG,
  INITIAL_ASSIGNMENTS,
  INITIAL_CLASSES,
  INITIAL_EXAMS,
  INITIAL_MODULES,
  buildFirestoreCatalogPayload,
  buildFirestoreManifestPayload
} from "../public/desktop/curriculum-data.js";
import { generateQrMatrix, renderQrSvg } from "../public/qr.js";

test("Desktop curriculum preserves exact 12 modules and 48 lectures split Part 1 (28) and Part 2 (20)", () => {
  assert.equal(INITIAL_MODULES.length, 12);
  assert.equal(INITIAL_CLASSES.length, 48);

  const part1Modules = new Set(INITIAL_MODULES.filter((m) => m.part === "PART_1_BASIC").map((m) => m.id));
  const part2Modules = new Set(INITIAL_MODULES.filter((m) => m.part === "PART_2_ADVANCE").map((m) => m.id));
  assert.equal(part1Modules.size, 6);
  assert.equal(part2Modules.size, 6);

  const part1Classes = INITIAL_CLASSES.filter((c) => part1Modules.has(c.moduleId));
  const part2Classes = INITIAL_CLASSES.filter((c) => part2Modules.has(c.moduleId));
  assert.equal(part1Classes.length, 28);
  assert.equal(part2Classes.length, 20);
});

test("Desktop role integrity separates Founder Md. Amirul Islam and Developer Md. Nafiul Islam Nahin", () => {
  assert.equal(DEFAULT_FOUNDER.name, "Md. Amirul Islam");
  assert.equal(DEFAULT_FOUNDER.designation, "Founder of ISCEB");
  assert.equal(DEFAULT_DEVELOPER.name, "Md. Nafiul Islam Nahin");
  assert.equal(DEFAULT_DEVELOPER.designation, "Developer of ISCEB App");
  assert.ok(!DEFAULT_DEVELOPER.designation.includes("Founder"));

  const admins = AVAILABLE_USERS.filter((u) => u.role === "ADMIN");
  assert.equal(admins.length, 2);
  assert.equal(admins[0].fullName, "Md. Amirul Islam");
  assert.equal(admins[1].fullName, "Md. Nafiul Islam Nahin");
});

test("Desktop assessments preserve exact marks allocation", () => {
  const byId = Object.fromEntries(INITIAL_EXAMS.map((e) => [e.id, e]));
  assert.equal(byId["exam-mod-1"].totalMarks, 20);
  assert.equal(byId["exam-mod-2"].totalMarks, 25);
  assert.equal(byId["exam-mod-3"].totalMarks, 30);
  assert.equal(byId["exam-mod-6"].totalMarks, 25);
  assert.equal(byId["exam-final"].totalMarks, 100);

  const finalAsg = INITIAL_ASSIGNMENTS.find((a) => a.id === "asg-12-final-project");
  assert.ok(finalAsg);
  assert.equal(finalAsg.maxMarks, 50);
  assert.equal(finalAsg.isFinalProject, true);
});

test("Firestore payload builders strictly comply with firestore.rules create/update schemas", () => {
  const tsToken = { _type: "serverTimestamp" };
  const createManifest = buildFirestoreManifestPayload({
    authorUid: "admin-uid-123",
    versionCode: 106,
    versionTag: "v1.5.1",
    publishedBy: "Md. Nafiul Islam Nahin (Developer of ISCEB App)",
    releaseNotes: "Desktop & Android sync release",
    announcement: "Live Class",
    latestApkVersionCode: 6,
    latestApkVersionName: "1.5",
    apkDownloadUrl: FIREBASE_CONFIG.apkDownloadUrl,
    isCreate: true,
    serverTimestampToken: tsToken
  });

  const requiredCreateKeys = [
    "authorUid", "versionCode", "versionTag", "publishedTimestamp",
    "publishedBy", "releaseNotes", "minSupportedAppVersion",
    "latestApkVersionCode", "latestApkVersionName", "apkDownloadUrl",
    "createdAt", "updatedAt"
  ];
  for (const k of requiredCreateKeys) {
    assert.ok(k in createManifest, `Missing required manifest key: ${k}`);
  }

  const updateCatalog = buildFirestoreCatalogPayload({
    authorUid: "admin-uid-123",
    versionCode: 106,
    isCreate: false,
    serverTimestampToken: tsToken
  });
  assert.ok(!("authorUid" in updateCatalog), "Update payload must not mutate authorUid");
  assert.ok(!("createdAt" in updateCatalog), "Update payload must not mutate createdAt");
  assert.equal(updateCatalog.modules.length, 12);
  assert.equal(updateCatalog.classes.length, 48);
});

test("Self-contained QR generator renders valid ISO finder patterns and SVG for permanent download URLs", () => {
  const url = "https://gen-lang-client-0901479759.web.app/";
  const matrix = generateQrMatrix(url);
  assert.ok(matrix.length >= 25 && matrix.length % 4 === 1);
  // Top-left 7x7 finder corners must be dark
  assert.equal(matrix[0][0], true);
  assert.equal(matrix[0][6], true);
  assert.equal(matrix[6][0], true);
  assert.equal(matrix[3][3], true);
  assert.equal(matrix[1][1], false);
  const svg = renderQrSvg(url, 168);
  assert.ok(svg.startsWith("<svg "));
  assert.ok(svg.includes("rect"));
});

